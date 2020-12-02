package org.mule.extension.slack.internal.operations;

import static java.lang.String.join;
import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.utils.CursorPagingProvider;
import org.mule.extension.slack.internal.valueprovider.ConversationsValueProvider;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConversationOperations extends SlackOperations {

    @MediaType(MediaType.APPLICATION_JSON)
    @OutputJsonType(schema = "metadata/conversations-info-schema.json")
    @DisplayName("Conversations - Info")
    public void conversationsInfo(@Connection SlackConnection slackConnection,
                                  @OfValues(ConversationsValueProvider.class) String channel,
                                  @Optional(defaultValue = "false") boolean includeLocal,
                                  @Optional(defaultValue = "false") boolean includeNumMembers,
                                  CompletionCallback<InputStream, Void> callback) {

        slackConnection.conversations.info(channel, includeLocal, includeNumMembers)
                .whenCompleteAsync(createConsumer("#[payload.channel]", EXECUTION, callback));
    }

    @OutputJsonType(schema = "metadata/conversations-info-schema.json")
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Conversations - Invite")
    public void conversationsInvite(@Connection SlackConnection slackConnection,
                                    @OfValues(ConversationsValueProvider.class) String channel,
                                    List<String> users,
                                    CompletionCallback<InputStream, Void> callback) {

        slackConnection.conversations.invite(channel, join(",", users))
                .whenCompleteAsync(createConsumer("#[payload.channel]", EXECUTION, callback));
    }

    @OutputJsonType(schema = "metadata/conversations-info-schema.json")
    @DisplayName("Conversations - Kick")
    @MediaType(MediaType.APPLICATION_JSON)
    public void conversationsKick(@Connection SlackConnection slackConnection,
                                  @OfValues(ConversationsValueProvider.class) String channel,
                                  String user,
                                  CompletionCallback<InputStream, Void> callback) {

        slackConnection.conversations.kick(channel, user)
                .whenCompleteAsync(createConsumer("#[payload.channel]", EXECUTION, callback));
    }

    @MediaType(MediaType.APPLICATION_JSON)
    @OutputJsonType(schema = "metadata/conversations-info-schema.json")
    @DisplayName("Conversations - Rename")
    public void renameConversations(@Connection SlackConnection slackConnection,
                                    @OfValues(ConversationsValueProvider.class) String channel,
                                    String name,
                                    CompletionCallback<InputStream, Void> callback) {

        slackConnection.conversations.rename(channel, name)
                .whenCompleteAsync(createConsumer("#[payload.channel]", EXECUTION, callback));
    }


    @MediaType(MediaType.APPLICATION_JSON)
    @OutputJsonType(schema = "metadata/topic-schema.json")
    @DisplayName("Conversations - Set Topic")
    public void setTopicConversations(@Connection SlackConnection slackConnection,
                                      @OfValues(ConversationsValueProvider.class) String channel,
                                      String topic,
                                      CompletionCallback<InputStream, Void> callback) {

        slackConnection.conversations.setTopic(channel, topic)
                .whenCompleteAsync(createConsumer("#[payload.topic]", EXECUTION, callback));
    }

    @MediaType(MediaType.APPLICATION_JSON)
    @OutputJsonType(schema = "metadata/purpose-schema.json")
    @DisplayName("Conversations - Set Purpose")
    public void setPurposeConversations(@Connection SlackConnection slackConnection,
                                        @OfValues(ConversationsValueProvider.class) String channel,
                                        String purpose,
                                        CompletionCallback<InputStream, Void> callback) {

        slackConnection.conversations.setPurpose(channel, purpose)
                .whenCompleteAsync(createConsumer("#[payload.topic]", EXECUTION, callback));
    }

    @DisplayName("Conversations - Archive")
    public void archiveConversation(@Connection SlackConnection slackConnection,
                                    @OfValues(ConversationsValueProvider.class) String channel,
                                    CompletionCallback<Void, Void> callback) {

        slackConnection.conversations.archive(channel)
                .whenCompleteAsync(createConsumer("#[payload.ok]", EXECUTION, callback));
    }

    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Conversations - Unarchive")
    public void unarchiveConversation(@Connection SlackConnection slackConnection,
                                      @OfValues(ConversationsValueProvider.class) String channel,
                                      CompletionCallback<Void, Void> callback) {

        slackConnection.conversations.unarchive(channel)
                .whenCompleteAsync(createConsumer("#[payload.ok]", EXECUTION, callback));
    }

    @MediaType(MediaType.APPLICATION_JSON)
    @OutputJsonType(schema = "metadata/conversations-open-schema.json")
    @DisplayName("Conversations - Open")
    public void openConversation(@Connection SlackConnection slackConnection,
                                 @ParameterGroup(name = "Open Conversation") ConversationOpenGroup group,
                                 @DisplayName("Return IM Definition") @Optional(defaultValue = "false") boolean returnImDefinition,
                                 CompletionCallback<InputStream, InputStream> callback) {

        slackConnection.conversations.open(group.getChannel(), group.getUsers(), returnImDefinition)
                .whenCompleteAsync(createConsumer("#[payload.channel]", "#[payload - \"channel\" - \"ok\"]", EXECUTION, callback));
    }

    @DisplayName("Conversations - Close")
    public void closeConversation(@Connection SlackConnection slackConnection,
                                 @OfValues(ConversationsValueProvider.class) String channel,
                                 CompletionCallback<Void, Void> callback) {

        slackConnection.conversations.close(channel)
                .whenCompleteAsync(createConsumer("#[payload]", EXECUTION, callback));
    }

    @OutputJsonType(schema = "metadata/topic-schema.json")
    @MediaType(MediaType.ANY)
    @DisplayName("Conversations - Members")
    public PagingProvider<SlackConnection, Map<String, Object>> getConversationMembers(@OfValues(ConversationsValueProvider.class) String channel) {
        return new CursorPagingProvider((connection, theCursor) -> connection.conversations.members(channel, null, theCursor), "#[output application/java --- payload.members]", expressionManager);

    }

    @OutputJsonType(schema = "metadata/conversations-schema.json")
    @DisplayName("Conversations - List")
    public PagingProvider<SlackConnection, Map<String, Object>> listConversations(@DisplayName("Public Channels") @Optional(defaultValue = "true") boolean publicChannels,
                                                                                  @DisplayName("Private Channels (Groups)") @Optional(defaultValue = "true") boolean privateChannels,
                                                                                  @DisplayName("Multi Party IM (MPIM)") @Optional(defaultValue = "true") boolean mpim,
                                                                                  @DisplayName("IM") @Optional(defaultValue = "true") boolean im,
                                                                                  @DisplayName("Exclude Archived Channels") @Optional(defaultValue = "true") boolean excludeArchived) {

        return new CursorPagingProvider((connection, theCursor) -> connection.conversations.list(excludeArchived, getChannelTypes(publicChannels, privateChannels, mpim, im), 200, theCursor), "#[output application/java --- payload.channels]", expressionManager);
    }

    @OutputJsonType(schema = "metadata/conversations-history-schema.json")
    @DisplayName("Conversations - History")
    public PagingProvider<SlackConnection, Map<String, Object>> conversationHistory(@OfValues(ConversationsValueProvider.class) String channel,
                                                                                    @Optional(defaultValue = "#[now() as Number]") String latest,
                                                                                    @Optional(defaultValue = "#[Slack::DateUtils::yesterday()]") String oldest,
                                                                                    @Optional(defaultValue = "true") boolean inclusive
    ) {
        return new CursorPagingProvider((connection, theCursor) -> connection.conversations.history(channel, latest, oldest, inclusive, 200, theCursor), "#[output application/java --- payload.messages]", expressionManager);
    }

    private String getChannelTypes(boolean publicChannels, boolean privateChannels, boolean mpim, boolean im) {
        ArrayList<String> strings = new ArrayList<>();
        if (publicChannels) {
            strings.add("public_channel");
        }
        if (privateChannels) {
            strings.add("private_channel");
        }
        if (mpim) {
            strings.add("mpim");
        }
        if (im) {
            strings.add("im");
        }
        return String.join(",", strings);
    }


}
