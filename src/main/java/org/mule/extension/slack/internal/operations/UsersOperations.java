package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.UsersErrorProvider;
import org.mule.extension.slack.internal.metadata.UsersInfoOutputResolver;
import org.mule.extension.slack.internal.metadata.UsersListOutputResolver;
import org.mule.extension.slack.internal.utils.CursorPagingProvider;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.io.InputStream;
import java.util.Map;

@Throws(UsersErrorProvider.class)
public class UsersOperations extends SlackOperations {

    /**
     * This operation returns a list of all users in the team. This includes deleted/deactivated users.
     *
     * @param includeLocale   Set this to true to receive the locale for users. Defaults to false
     * @param limit           The maximum number of items to return. Fewer than the requested number of items may be returned,
     *                        even if the end of the users list hasn't been reached.
     * @param presence        Optional
     *                        Whether to include presence data in the output. Setting this to false improves performance, especially with large
     *                        teams.
     */
    @OutputResolver(output = UsersListOutputResolver.class)
    @DisplayName("User - List")
    public PagingProvider<SlackConnection, Map<String, Object>> listUsers(@Optional(defaultValue = "false") boolean includeLocale,
                                                                          @Optional(defaultValue = "0") int limit,
                                                                          @Optional(defaultValue = "false") boolean presence,
                                                                          @Optional @DisplayName("Cursor (Deprecated)") @Placement(tab = "Deprecated") String cursor) {

      return new CursorPagingProvider((connection, theCursor) -> connection.user.list(theCursor, includeLocale, limit, presence), "#[output application/java --- payload.members]", expressionManager);
    }

    /**
     * This method returns information about a member of a workspace.
     *
     * @param slackConnection The connection
     * @param user            User to get info on
     * @param includeLocale   Set this to true to receive the locale for this user. Defaults to false
     * @param callback
     */
    @OutputResolver(output = UsersInfoOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("User - Info")
    public void userInfo(@Connection SlackConnection slackConnection,
                         @Optional @Example("W1234567890 or @john") String user,
                         @Optional(defaultValue = "false") boolean includeLocale,
                         CompletionCallback<InputStream, Void> callback) {
        slackConnection.user
                .info(user, includeLocale)
                .whenCompleteAsync(createConsumer("#[payload.user]", EXECUTION, callback));
    }
}
