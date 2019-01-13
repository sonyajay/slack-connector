package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.CHANNEL_LISTING;

import org.mule.extension.slack.internal.ConversationListingConfiguration;
import org.mule.extension.slack.internal.ConversationTypes;
import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.ChannelListErrorProvider;
import org.mule.extension.slack.internal.metadata.ListConversationsOutputResolver;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class ConversationOperations extends SlackOperations {

    @Throws(ChannelListErrorProvider.class)
    @OutputResolver(output = ListConversationsOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    public void listConversations(@Connection SlackConnection slackConnection,
//                                  @MetadataKeyId(ConversationTypesKeysResolver.class)
                                  @ParameterGroup(name = "Conversation Types") ConversationTypes conversationTypes,
                                  @ParameterGroup(name = "Listing Configuration") ConversationListingConfiguration listingConfiguration,
                                  CompletionCallback<InputStream, Void> callback) {

        slackConnection
                .conversationList(conversationTypes, listingConfiguration)
                .whenCompleteAsync(createConsumer("#[payload.channels]", "#[payload.response_metadata]", CHANNEL_LISTING, callback));
    }
}
