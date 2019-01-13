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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MetadataScope(outputResolver = ListenerOutputResolver.class)
@Alias("message-listener")
@DisplayName("On New Message")
@MediaType(APPLICATION_JSON)
public class SlackMessageReceiver extends Source<String, Void> {

    Logger LOGGER = LoggerFactory.getLogger(SlackMessageReceiver.class);

    @Inject
    private SchedulerService schedulerService;

    @Connection
    private ConnectionProvider<SlackConnection> connectionProvider;

    @ParameterGroup(name = "Messages Event Matcher")
    private SlackMessageEventMatcher messageEventMatcher;

    private Scheduler scheduler;

    @Override
    public void onStart(SourceCallback<String, Void> sourceCallback) throws MuleException {
        LOGGER.debug("Starting Slack RTM (Real Time Communication)");
        SlackConnection slackConnection = connectionProvider.connect();
        doStart(sourceCallback, slackConnection);
    }

    private void doStart(SourceCallback<String, Void> sourceCallback, SlackConnection slackConnection) {
        CompletableFuture<HttpResponse> wsUri = slackConnection
                .getWebSocketURI();

        List<EventNotifier> eventNotifiers = new ArrayList<>();
        eventNotifiers.add(new MessagesNotifier(messageEventMatcher));

        wsUri
                .whenCompleteAsync(((httpResponse, throwable) -> {
                    if(throwable != null) {
                        LOGGER.error("An error occurred trying to obtain RTM WSS URL.", throwable);
                        scheduler.schedule(() -> doStart(sourceCallback, slackConnection), 10, TimeUnit.SECONDS);
                    } else {
                        String response = IOUtils.toString(httpResponse.getEntity().getContent());
                        String url = new JSONObject(response).getString("url");
                        this.scheduler = schedulerService.ioScheduler();
                        SlackMessageHandler messageHandler = new SlackMessageHandler(url, new ConfigurableHandler(sourceCallback, eventNotifiers, emptyList()), scheduler, () -> doStart(sourceCallback, slackConnection));
                        this.scheduler.execute(() -> {
                            try {
                                messageHandler.connect();
                            } catch (Exception e) {
                                sourceCallback.onConnectionException(new ConnectionException(e, slackConnection));
                            }
                        });
                    }
                }));
    }

    @Override
    public void onStop() {
        if (scheduler != null) {
            scheduler.stop();
        }
    }
}
