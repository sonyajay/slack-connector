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
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class GroupOperations extends SlackOperations {

    /**
     * Lists private channels that the calling user has access to.
     * </p>
     * This method returns a list of private channels in the team that the caller is in and archived groups that the
     * caller was in. The list of (non-deactivated) members in each private channel is also returned.
     *
     * @param slackConnection The Connection
     * @param excludeArchived Don't return archived private channels.
     * @param excludeMembers  Exclude the members from each group
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

    /**
     * This operation returns information about a private channel.
     *
     * @param slackConnection The connection
     * @param channel         Private channel to get info on
     * @param includeLocal    Set this to true to receive the locale for this group. Defaults to false
     * @param callback
     */
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

    /**
     * This operation renames a private channel.
     *
     * @param slackConnection The connection
     * @param channel         Private channel to rename
     * @param name            New name for private channel.
     * @param validate        Whether to return errors on invalid channel name instead of modifying it to meet the specified criteria.
     * @param callback
     */
    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = RenameGroupOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - Rename")
    public void renameGroup(@Connection SlackConnection slackConnection,
                            @OfValues(GroupsValueProvider.class) String channel,
                            @Example("mule-engineers-group") String name,
                            @Optional(defaultValue = "false") boolean validate,
                            CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .rename(channel, name, validate)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.channel]", EXECUTION, callback));
    }

    /**
     * This operation is used to change the purpose of a private channel. The calling user must be a member of the private
     * channel.
     *
     * @param slackConnection The connection
     * @param channel         Private channel to set the purpose of
     * @param purpose         The new purpose
     * @param callback
     */
    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = StringOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - Set Purpose")
    public void setGroupPurpose(@Connection SlackConnection slackConnection,
                                @OfValues(GroupsValueProvider.class) String channel,
                                @Example("Group to talk about Mule products") String purpose,
                                CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .setPurpose(channel, purpose)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.purpose]", EXECUTION, callback));
    }

    /**
     * This operation is used to change the topic of a private channel. The calling user must be a member of the private channel.
     *
     * @param slackConnection The connection
     * @param channel         Private channel to set the topic of
     * @param topic
     * @param callback        The new topic
     */
    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = StringOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Groups - Set Topic")
    public void setGroupTopic(@Connection SlackConnection slackConnection,
                              @OfValues(GroupsValueProvider.class) String channel,
                              @Example("Group to talk about Mule products") String topic,
                              CompletionCallback<InputStream, Void> callback) {
        slackConnection.group
                .setTopic(channel, topic)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.topic]", EXECUTION, callback));
    }
}
