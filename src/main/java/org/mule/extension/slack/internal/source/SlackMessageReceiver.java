package org.mule.extension.slack.internal.source;

import static java.util.Collections.emptyList;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.source.rtm.ConfigurableHandler;
import org.mule.extension.slack.internal.source.rtm.SlackMessageHandler;
import org.mule.extension.slack.internal.source.rtm.filter.EventNotifier;
import org.mule.extension.slack.internal.source.rtm.filter.MessagesNotifier;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.scheduler.Scheduler;
import org.mule.runtime.api.scheduler.SchedulerService;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.MetadataScope;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import javax.inject.Inject;
import javax.websocket.DeploymentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

@MetadataScope(outputResolver = ListenerOutputResolver.class)
@Alias("message-listener")
@DisplayName("Message Receiver")
@MediaType(APPLICATION_JSON)
public class SlackMessageReceiver extends Source<String, Void> {

    @Inject
    private SchedulerService schedulerService;

    @Connection
    private ConnectionProvider<SlackConnection> connectionProvider;

    @ParameterGroup(name = "Messages Event Matcher")
    private SlackMessageEventMatcher messageEventMatcher;

    private Scheduler scheduler;

    @Override
    public void onStart(SourceCallback<String, Void> sourceCallback) throws MuleException {
        SlackConnection slackConnection = connectionProvider.connect();
        CompletableFuture<HttpResponse> webSockerURI1 = slackConnection
                .getWebSocketURI();

        List<EventNotifier> eventNotifiers = new ArrayList<>();
        eventNotifiers.add(new MessagesNotifier(messageEventMatcher));

        webSockerURI1
                .whenCompleteAsync(((httpResponse, throwable) -> {
                    String response = IOUtils.toString(httpResponse.getEntity().getContent());
                    String url = new JSONObject(response).getString("url");
                    SlackMessageHandler messageHandler = new SlackMessageHandler(url, new ConfigurableHandler(sourceCallback, eventNotifiers, emptyList()));
                    this.scheduler = schedulerService.cpuLightScheduler();
                    this.scheduler.execute(() -> {
                        try {
                            messageHandler.connect();
                        } catch (IOException | DeploymentException | InterruptedException e) {
                            sourceCallback.onConnectionException(new ConnectionException(e, slackConnection));
                        }
                    });
                }));
    }

    @Override
    public void onStop() {
        if (scheduler != null) {
            scheduler.stop();
        }
    }
}
