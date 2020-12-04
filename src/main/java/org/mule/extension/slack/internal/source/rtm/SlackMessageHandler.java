/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.slack.internal.source.rtm;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.mule.runtime.api.scheduler.Scheduler;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackMessageHandler implements MessageHandler.Whole<String> {

    Logger LOGGER = LoggerFactory.getLogger(SlackMessageHandler.class);

    public EventHandler messageHandler;
    private Scheduler scheduler;
    private Runnable onClose;
    private String webSocketUrl;
    private Session websocketSession;
    private long lastPingSent = 0;
    private volatile long lastPingAck = 0;
    private boolean reconnectOnDisconnection = true;
    private long messageId = 0;
    private ScheduledFuture pingScheduler;

    public SlackMessageHandler(String webSocketUrl, EventHandler messageHandler, Scheduler scheduler, Runnable onClose) {
        this.webSocketUrl = webSocketUrl;
        this.messageHandler = messageHandler;
        this.scheduler = scheduler;
        this.onClose = onClose;
    }

    public void connect() throws IOException, DeploymentException {
        ClientManager client = ClientManager.createClient();
        client.getProperties().put(ClientProperties.LOG_HTTP_UPGRADE, false);
        final MessageHandler handler = this;
        websocketSession = client.connectToServer(new Endpoint() {

            @Override
            public void onOpen(Session session, EndpointConfig config) {
                LOGGER.debug("On Open");
                session.addMessageHandler(handler);
            }

            @Override
            public void onClose(Session session, CloseReason closeReason) {
                LOGGER.debug("On Close");

                pingScheduler.cancel(true);
                scheduler.submit(onClose);
            }

            @Override
            public void onError(Session session, Throwable e) {
                LOGGER.error("Unexpected error. ", e);
            }
        }, URI.create(webSocketUrl));

        pingScheduler = scheduler.scheduleAtFixedRate(() -> {
            try {
                LOGGER.debug("PING");
                if (lastPingSent != lastPingAck) {
                    // disconnection happened
                    websocketSession.close();
                    lastPingSent = 0;
                    lastPingAck = 0;
                } else {
                    lastPingSent = getNextMessageId();
                    websocketSession.getBasicRemote().sendText("{\"type\":\"ping\",\"id\":" + lastPingSent + "}");
                }
            } catch (Exception e) {
                try {
                    LOGGER.error("CLOSING", e);
                    websocketSession.close();
                } catch (IOException e1) {
                    LOGGER.debug("The Slack WebSocket connection seems to be disconnected. Reconnecting.");
                }
            }
        }, 0, 20, SECONDS);
    }

    public void onMessage(String message) {
        LOGGER.debug("Received message: {}", message);
        if (message.startsWith("{\"type\":\"pong\",\"reply_to\"")) {
            int rightBracketIdx = message.indexOf('}');
            String toParse = message.substring(26, rightBracketIdx);
            lastPingAck = Integer.parseInt(toParse);
            return;
        }
        if (!message.startsWith("{\"type\":\"hello\"}")) {
            messageHandler.onMessage(message);
        }
    }

    private synchronized long getNextMessageId() {
        return messageId++;
    }

}
