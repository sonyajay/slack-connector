package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.PUBLISHING;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.api.ParsingMode;
import org.mule.extension.slack.internal.MessageConfigurationGroup;
import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.PostMessageErrorProvider;
import org.mule.extension.slack.internal.metadata.AttachmentsTypeResolver;
import org.mule.extension.slack.internal.metadata.ChannelsKeyResolver;
import org.mule.extension.slack.internal.metadata.PostMessageAttributesResolver;
import org.mule.extension.slack.internal.metadata.PostMessageOutputResolver;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class ChatOperations extends SlackOperations {

    /**
     * This operation lets you post a message to a public channel, private channel, or direct message/IM channel.
     *
     * @param slackConnection      The connection
     * @param channel              Channel, private group, or IM channel to send message to. Can be an encoded ID, or
     *                             a name.
     * @param message              New text for the message. It's not required when presenting attachments.
     * @param attachments          A JSON-based array of structured attachments, presented as a URL-encoded string.
     *                             This field is required when not presenting text.
     * @param messageConfiguration Group of parameter
     * @param callback             Non-blocking callback
     */
    @Throws(PostMessageErrorProvider.class)
    @OutputResolver(output = PostMessageOutputResolver.class, attributes = PostMessageAttributesResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Chat - Post Message")
    public void postMessage(@Connection SlackConnection slackConnection,
                            @OfValues(ChannelsKeyResolver.class) String channel,
                            @Optional @Content(primary = true) String message,
                            @Content @Optional @TypeResolver(AttachmentsTypeResolver.class) InputStream attachments,
                            @ParameterGroup(name = "Message Configuration") MessageConfigurationGroup messageConfiguration,
                            CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.chat
                .postMessage(message, channel, attachments, messageConfiguration.getUsername(), messageConfiguration)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.message]", "#[payload - 'message' - 'ok']", PUBLISHING, callback));
    }

    /**
     * This operation updates a message in a channel.
     * </p>
     * Ephemeral messages created by chat.postEphemeral or otherwise cannot be updated with this method.
     *
     * @param slackConnection The connection
     * @param channel         Channel containing the message to be updated.
     * @param message         New text for the message. It's not required when presenting attachments.
     * @param attachments     A JSON-based array of structured attachments, presented as a URL-encoded string. This
     *                        field is required when not presenting text.
     * @param timestamp       Timestamp of the message to be updated.
     * @param asUser          Pass true to update the message as the authed user. Bot users in this context are
     *                        considered authed users.
     * @param linkNames       Find and link channel names and usernames. Defaults to none. This parameter should be used
     *                        in conjunction with parse. To set link_names to TRUE, specify a parse mode of FULL.
     * @param parse           Change how messages are treated. See: https://api.slack.com/methods/chat.update#formatting
     * @param callback
     */
    @Throws(PostMessageErrorProvider.class)
    @OutputResolver(output = PostMessageOutputResolver.class, attributes = PostMessageAttributesResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Chat - Update Message")
    public void update(@Connection SlackConnection slackConnection,
                       @OfValues(ChannelsKeyResolver.class) String channel,
                       @Optional @Content(primary = true) String message,
                       @Content @Optional @TypeResolver(AttachmentsTypeResolver.class) InputStream attachments,
                       @Example("1405894322.002768") String timestamp,
                       @Optional(defaultValue = "false") boolean asUser,
                       @Optional(defaultValue = "false") boolean linkNames,
                       @Optional(defaultValue = "NONE") ParsingMode parse,
                       CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.chat
                .update(message, channel, attachments, timestamp, asUser, linkNames, parse)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.message]", "#[payload - 'message' - 'ok']", PUBLISHING, callback));
    }
}
