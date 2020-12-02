package org.mule.extension.slack.internal.operations;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.metadata.ChannelInfoOutputResolver;
import org.mule.extension.slack.internal.metadata.ListChannelsOutputResolver;
import org.mule.extension.slack.internal.metadata.StringOutputResolver;
import org.mule.extension.slack.internal.utils.CursorPagingProvider;
import org.mule.extension.slack.internal.valueprovider.ChannelsValueProvider;
import org.mule.extension.slack.internal.valueprovider.UsersValueProvider;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.io.InputStream;
import java.util.Map;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

@Deprecated
public class ChannelOperations extends SlackOperations {

    /**
     * This method returns a list of all channels in the team. This includes channels the caller is in, channels they
     * are not currently in, and archived channels but does not include private channels. The number of
     * (non-deactivated) members in each channel is also returned.
     *
     * @param excludeArchived Exclude archived channels from the list
     * @param excludeMembers  Exclude the members collection from each channel
     */
    //TODO THIS CAN USE `excludeMembers` parameter to improve metadata.
    @OutputResolver(output = ListChannelsOutputResolver.class)
    @DisplayName("Channels - List (Deprecated)")
    public PagingProvider<SlackConnection, Map<String, Object>> listChannels(@Optional(defaultValue = "false") boolean excludeArchived,
                                                                             @Optional(defaultValue = "false") boolean excludeMembers) {

        return new CursorPagingProvider((connection, theCursor) -> connection.channel.list(theCursor, excludeArchived, excludeMembers, 100),"#[output application/java --- payload.channels]", this.expressionManager);
    }

    /**
     * Returns information about a team channel.
     * </p>
     * To retrieve information on a private channel, use group-info operation.
     *
     * @param slackConnection Connection
     * @param channel         Channel to get info on
     * @param includeLocale   Set this to true to receive the locale for this channel. Defaults to false
     * @param callback
     */
    @OutputJsonType(schema = "metadata/channel-info-schema.json")
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Info (Deprecated)")
    public void channelInfo(@Connection SlackConnection slackConnection,
                            @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                            @Optional(defaultValue = "false") boolean includeLocale,
                            CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .info(channel, includeLocale)
                .whenCompleteAsync(createConsumer("#[payload.channel]", EXECUTION, callback));
    }

    /**
     * This operation is used to change the topic of a channel. The calling user must be a member of the channel.
     *
     * @param slackConnection The connection
     * @param channel         Channel to set the topic of
     * @param topic           The new topic
     * @param callback
     */
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Set Topic (Deprecated)")
    @OutputResolver(output = StringOutputResolver.class)
    public void setChannelTopic(@Connection SlackConnection slackConnection,
                                @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                                @Example("Channel to talk about the Application Network")  String topic,
                                CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .setTopic(channel, topic)
                .whenCompleteAsync(createConsumer("#[payload.topic]", EXECUTION, callback));
    }

    /**
     * This operation is used to change the purpose of a channel. The calling user must be a member of the channel.
     *
     * @param slackConnection The connection
     * @param channel         Channel to set the purpose of
     * @param purpose         The new purpose
     * @param callback
     */
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Set Purpose (Deprecated)")
    @OutputResolver(output = StringOutputResolver.class)
    public void setChannelPurpose(@Connection SlackConnection slackConnection,
                                  @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                                  @Example("Channel to talk about the Application Network") String purpose,
                                  CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .setPurpose(channel, purpose)
                .whenCompleteAsync(createConsumer("#[payload.purpose]", EXECUTION, callback));
    }

    /**
     * This method renames a team channel.
     * </p>
     * The only people who can rename a channel are Team Admins, or the person that originally created the channel.
     * Others will receive a "not_authorized" error.
     *
     * @param slackConnection The Connection
     * @param channel         Channel to rename
     * @param name            New name for channel.
     * @param validate        Whether to return errors on invalid channel name instead of modifying it to meet the
     *                        specified criteria.
     * @param callback
     */
    @OutputResolver(output = ChannelInfoOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Rename (Deprecated)")
    public void renameChannel(@Connection SlackConnection slackConnection,
                              @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                              @Example("mule-engineers-channel") String name,
                              @Optional(defaultValue = "false") boolean validate,
                              CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .rename(channel, name, validate)
                .whenCompleteAsync(createConsumer("#[payload.channel]", EXECUTION, callback));
    }

    /**
     * This method is used to invite a user to a channel. The calling user must be a member of the channel.
     *
     * @param slackConnection The Connection
     * @param channel         Channel to invite user to.
     * @param user            User to invite to channel.
     * @param callback
     */
    @OutputResolver(output = ChannelInfoOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Invite (Deprecated)")
    public void inviteToChannel(@Connection SlackConnection slackConnection,
                                @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                                @OfValues(UsersValueProvider.class) String user,
                                CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .invite(channel, user)
                .whenCompleteAsync(createConsumer("#[payload.channel]", EXECUTION, callback));
    }
}
