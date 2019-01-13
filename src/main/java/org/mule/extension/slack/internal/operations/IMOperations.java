package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.metadata.IMListOutputResolver;
import org.mule.extension.slack.internal.metadata.OpenIMOutputResolver;
import org.mule.extension.slack.internal.utils.CursorPagingProvider;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.io.InputStream;
import java.util.Map;

public class IMOperations extends SlackOperations {

    /**
     * This operation returns a list of all im channels that the user has.
     *
     * @param slackConnection The connection
     * @param cursor
     * @param limit           The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of
     *                        the users list hasn't been reached.
     */
    @OutputResolver(output = IMListOutputResolver.class)
    @DisplayName("IM - List")
    public PagingProvider<SlackConnection, Map<String, Object>> listIms(@Optional @Placement(tab = "Deprecated") @DisplayName("Cursor (Deprecated)") String cursor,
                                                                        @Optional(defaultValue = "0") int limit) {

        return new CursorPagingProvider((connection, theCursor) -> connection.im.list(theCursor, limit),"#[output application/java --- payload.ims]", this.expressionManager);
    }

    /**
     * This operation opens a direct message channel with another member of your Slack team.
     *
     * @param slackConnection
     * @param user            User to open a direct message channel with.
     * @param includeLocale   Set this to true to receive the locale for this im. Defaults to false
     * @param returnIm        Boolean, indicates you want the full IM channel definition in the response.
     * @param callback
     */
    @MediaType(APPLICATION_JSON)
    @OutputResolver(output = OpenIMOutputResolver.class, attributes = OpenIMOutputResolver.class)
    @DisplayName("IM - Open")
    @Alias("open-im")
    public void openIM(@Connection SlackConnection slackConnection,
                       String user,
                       @Optional(defaultValue = "FALSE") boolean includeLocale,
                       @Optional(defaultValue = "FALSE") @MetadataKeyId boolean returnIm,
                       CompletionCallback<InputStream, InputStream> callback) {

        slackConnection.im
                .open(user, includeLocale, returnIm)
                .whenCompleteAsync(createConsumer("#[payload.channel]", "#[payload - 'channel' - 'ok']", EXECUTION, callback));
    }
}
