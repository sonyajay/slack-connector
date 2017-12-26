package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.metadata.IMListOutputResolver;
import org.mule.extension.slack.internal.metadata.OpenIMOutputResolver;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class IMOperations extends SlackOperations {

    @OutputResolver(output = IMListOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("IM - List")
    public void listIms(@Connection SlackConnection slackConnection,
                        @Optional String cursor,
                        @Optional(defaultValue = "0") int limit,
                        CompletionCallback<InputStream, Void> callback) {

        slackConnection.im
                .list(cursor, limit)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.ims]", "#[payload.response_metadata]", EXECUTION, callback));
    }

    @MediaType(APPLICATION_JSON)
    @OutputResolver(output = OpenIMOutputResolver.class, attributes = OpenIMOutputResolver.class)
    @DisplayName("IM - Open")
    public void openIM(@Connection SlackConnection slackConnection,
                       String user,
                       @Optional(defaultValue = "FALSE") boolean includeLocale,
                       @Optional(defaultValue = "FALSE") @MetadataKeyId boolean returnIm,
                       CompletionCallback<InputStream, InputStream> callback) {

        slackConnection.im
                .open(user, includeLocale, returnIm)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.channel]", "#[payload - 'channel' - 'ok']", EXECUTION, callback));
    }
}
