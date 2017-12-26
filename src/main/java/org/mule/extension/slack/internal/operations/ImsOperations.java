package org.mule.extension.slack.internal.operations;

import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.ChannelListErrorProvider;
import org.mule.extension.slack.internal.metadata.ListGroupsOutputResolver;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class ImsOperations extends SlackOperations {

    @Throws(ChannelListErrorProvider.class)
    @OutputResolver(output = ListGroupsOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    public void listIms(@Connection SlackConnection slackConnection,
                        @Optional(defaultValue = "false") boolean excludeArchived,
                        @Optional(defaultValue = "false") boolean excludeMembers,
                        CompletionCallback<InputStream, Void> callback) {
//        slackConnection
//                .imsList(excludeArchived, excludeMembers)
//                .whenCompleteAsync(new HttpResponseConsumer("#[payload.ims]", CHANNEL_LISTING, callback));
    }
}
