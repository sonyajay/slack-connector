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
     * @param message              Text of the message to send. This field is usually required, unless you're providing
     *                             only attachments instead.
     * @param attachments          A JSON-based array of structured attachments, presented as a URL-encoded string.
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

    @Throws(PostMessageErrorProvider.class)
    @OutputResolver(output = PostMessageOutputResolver.class, attributes = PostMessageAttributesResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Chat - Update Message")
    public void update(@Connection SlackConnection slackConnection,
                       @OfValues(ChannelsKeyResolver.class) String channel,
                       @Optional @Content(primary = true) String message,
                       @Content @Optional @TypeResolver(AttachmentsTypeResolver.class) InputStream attachments,
                       String timestamp,
                       @Optional(defaultValue = "false") boolean asUser,
                       @Optional(defaultValue = "false") boolean linkNames,
                       @Optional(defaultValue = "NONE") ParsingMode parse,
                       CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.chat
                .update(message, channel, attachments, timestamp, asUser, linkNames, parse)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.message]", "#[payload - 'message' - 'ok']", PUBLISHING, callback));
    }
}
