package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.CHANNEL_LISTING;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.ChannelListErrorProvider;
import org.mule.extension.slack.internal.metadata.ChannelInfoOutputResolver;
import org.mule.extension.slack.internal.metadata.ListChannelsOutputResolver;
import org.mule.extension.slack.internal.metadata.StringOutputResolver;
import org.mule.extension.slack.internal.valueprovider.ChannelsValueProvider;
import org.mule.extension.slack.internal.valueprovider.UsersValueProvider;
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

public class ChannelOperations extends SlackOperations {

    /**
     * This method returns a list of all channels in the team. This includes channels the caller is in, channels they
     * are not currently in, and archived channels but does not include private channels. The number of
     * (non-deactivated) members in each channel is also returned.
     *
     * @param slackConnection The connection
     * @param cursor          Paginate through collections of data by setting the cursor parameter to a next_cursor attribute
     *                        returned by a previous request's response_metadata. Default value fetches the first "page" of the
     *                        collection. See pagination for more detail.
     * @param excludeArchived Exclude archived channels from the list
     * @param excludeMembers  Exclude the members collection from each channel
     * @param limit           The maximum number of items to return. Fewer than the requested number of items may be returned,
     *                        even if the end of the users list hasn't been reached.
     * @param callback        Non-blocking callback
     */
    @Throws(ChannelListErrorProvider.class)
    //TODO THIS CAN USE `excludeMembers` parameter to improve metadata.
    @OutputResolver(output = ListChannelsOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - List")
    public void listChannels(@Connection SlackConnection slackConnection,
                             @Optional String cursor,
                             @Optional(defaultValue = "false") boolean excludeArchived,
                             @Optional(defaultValue = "false") boolean excludeMembers,
                             @Optional(defaultValue = "0") int limit,
                             CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .list(cursor, excludeArchived, excludeMembers, limit)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.channels]", CHANNEL_LISTING, callback));
    }

    @OutputResolver(output = ChannelInfoOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Info")
    public void channelInfo(@Connection SlackConnection slackConnection,
                            @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                            @Optional(defaultValue = "false") boolean includeLocale,
                            CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .info(channel, includeLocale)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.channel]", CHANNEL_LISTING, callback));
    }

    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Set Topic")
    @OutputResolver(output = StringOutputResolver.class)
    public void setChannelTopic(@Connection SlackConnection slackConnection,
                                @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                                String topic,
                                CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .setTopic(channel, topic)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.topic]", CHANNEL_LISTING, callback));
    }

    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Set Purpose")
    @OutputResolver(output = StringOutputResolver.class)
    public void setChannelPurpose(@Connection SlackConnection slackConnection,
                                  @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                                  String purpose,
                                  CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .setPurpose(channel, purpose)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.purpose]", CHANNEL_LISTING, callback));
    }

    @OutputResolver(output = ChannelInfoOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Rename")
    public void renameChannel(@Connection SlackConnection slackConnection,
                              @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                              String name,
                              @Optional(defaultValue = "false") boolean validate,
                              CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .rename(channel, name, validate)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.channel]", CHANNEL_LISTING, callback));
    }

    @OutputResolver(output = ChannelInfoOutputResolver.class)
    @MediaType(MediaType.APPLICATION_JSON)
    @DisplayName("Channels - Invite")
    public void inviteToChannel(@Connection SlackConnection slackConnection,
                                @OfValues(ChannelsValueProvider.class) @Example("C1234567890") @Optional String channel,
                                @OfValues(UsersValueProvider.class) String user,
                                CompletionCallback<InputStream, Void> callback) {
        slackConnection.channel
                .invite(channel, user)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.channel]", CHANNEL_LISTING, callback));
    }
}
