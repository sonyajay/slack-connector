package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.CHANNEL_LISTING;
import static org.mule.extension.slack.internal.error.SlackError.DESCRIBING;
import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.ChannelListErrorProvider;
import org.mule.extension.slack.internal.error.DescribingErrorProvider;
import org.mule.extension.slack.internal.metadata.GroupInfoOutputResolver;
import org.mule.extension.slack.internal.metadata.ListGroupsOutputResolver;
import org.mule.extension.slack.internal.metadata.RenameGroupOutputResolver;
import org.mule.extension.slack.internal.metadata.StringOutputResolver;
import org.mule.extension.slack.internal.valueprovider.GroupsValueProvider;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class GroupOperations extends SlackOperations {

    /**
     * @param slackConnection
     * @param excludeArchived
     * @param excludeMembers
     * @param callback
     */
    @Throws(ChannelListErrorProvider.class)
    @OutputResolver(output = ListGroupsOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - List")
    public void listGroups(@Connection SlackConnection slackConnection,
                           @Optional(defaultValue = "false") boolean excludeArchived,
                           @Optional(defaultValue = "false") boolean excludeMembers,
                           CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .list(excludeArchived, excludeMembers)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.groups]", CHANNEL_LISTING, callback));
    }

    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = GroupInfoOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - Info")
    public void groupInfo(@Connection SlackConnection slackConnection,
                          @OfValues(GroupsValueProvider.class) String channel,
                          @Optional(defaultValue = "false") boolean includeLocal,
                          CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .info(channel, includeLocal)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.group]", DESCRIBING, callback));
    }

    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = RenameGroupOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - Rename")
    public void renameGroup(@Connection SlackConnection slackConnection,
                            @OfValues(GroupsValueProvider.class) String channel,
                            String name,
                            @Optional(defaultValue = "false") boolean validate,
                            CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .rename(channel, name, validate)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.channel]", EXECUTION, callback));
    }

    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = StringOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - Set Purpose")
    public void setGroupPuporse(@Connection SlackConnection slackConnection,
                                @OfValues(GroupsValueProvider.class) String channel,
                                String purpose,
                                CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .setPurpose(channel, purpose)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.purpose]", EXECUTION, callback));
    }

    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = StringOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - Set Topic")
    public void setGroupTopic(@Connection SlackConnection slackConnection,
                              @OfValues(GroupsValueProvider.class) String channel,
                              String topic,
                              CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .setTopic(channel, topic)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.topic]", EXECUTION, callback));
    }
}
