package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.utils.SlackUtils.getBindingContext;

import org.mule.extension.slack.internal.ConversationListingConfiguration;
import org.mule.extension.slack.internal.ConversationTypes;
import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.ChannelListErrorProvider;
import org.mule.extension.slack.internal.metadata.ListConversationsOutputResolver;
import org.mule.extension.slack.internal.utils.JsonListInputStream;
import org.mule.runtime.api.scheduler.Scheduler;
import org.mule.runtime.api.scheduler.SchedulerService;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import javax.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class XOperations extends SlackOperations {

    @Inject
    SchedulerService schedulerService;

    @Inject
    ExpressionManager expressionManager;

    @Throws(ChannelListErrorProvider.class)
    @OutputResolver(output = ListConversationsOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    public void listConversationsPaged(@Connection SlackConnection slackConnection,
//                                  @MetadataKeyId(ConversationTypesKeysResolver.class)
                                       @ParameterGroup(name = "Conversation Types") ConversationTypes conversationTypes,
                                       @ParameterGroup(name = "Listing Configuration") ConversationListingConfiguration listingConfiguration,
                                       CompletionCallback<InputStream, Void> callback) {

        JsonListInputStream inputStream = new JsonListInputStream(new Iterator<InputStream>() {

            boolean hasNext;
            String cursor;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public InputStream next() {

                if (cursor != null) {
                    listingConfiguration.setCursor(cursor);
                } else {
                    listingConfiguration.setCursor(null);
                }

                CountDownLatch latch = new CountDownLatch(1);
                Reference<Object> output = new Reference<>();

                slackConnection
                        .conversationList(conversationTypes, listingConfiguration)
                        .whenCompleteAsync(((httpResponse, throwable) -> {
                            InputStream content = httpResponse.getEntity().getContent();
                            CountDownLatch otherLatch = new CountDownLatch(1);
                            Reference<Map<String, Object>> result = new Reference<>();
                            Scheduler scheduler = schedulerService.ioScheduler();
                            scheduler.execute(() -> {
                                Map<String, Object> value = (Map<String, Object>) expressionManager.evaluate("#[output application/java --- { channels : write(payload.channels, \"application/json\"), cursor : payload.response_metadata.next_cursor}]", getBindingContext(content)).getValue();
                                result.set(value);
                                otherLatch.countDown();
                            });
                            try {
                                otherLatch.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                scheduler.stop();
                            }
                            Map<String, Object> value = result.get();
                            Object cursor = value.get("cursor");
                            if (cursor != null) {
                                this.cursor = (String) cursor;
                                hasNext = true;
                            } else {
                                hasNext = false;
                            }

                            output.set(value.get("channels"));
                            latch.countDown();

                        }));
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    return null;
                }
                if (output.get().equals("[\n" +
                        "  \n" +
                        "]")) {
                    hasNext = false;
                    return new ByteArrayInputStream("{}".getBytes());
                }
                return new ByteArrayInputStream(((String) output.get()).getBytes());
            }
        });

        callback.success(Result.<InputStream, Void>builder().output(inputStream).build());

    }
}
