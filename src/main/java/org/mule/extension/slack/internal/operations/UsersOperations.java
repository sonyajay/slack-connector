package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.USER_LISTING;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.DescribingErrorProvider;
import org.mule.extension.slack.internal.error.UserListingErrorProvider;
import org.mule.extension.slack.internal.error.UsersErrorProvider;
import org.mule.extension.slack.internal.metadata.UsersInfoOutputResolver;
import org.mule.extension.slack.internal.metadata.UsersListOutputResolver;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

@Throws(UsersErrorProvider.class)
public class UsersOperations extends SlackOperations {

    /**
     * This operation returns a list of all users in the team. This includes deleted/deactivated users.
     *
     * @param slackConnection The connection
     * @param cursor
     * @param includeLocale   Set this to true to receive the locale for users. Defaults to false
     * @param limit           The maximum number of items to return. Fewer than the requested number of items may be returned,
     *                        even if the end of the users list hasn't been reached.
     * @param presence        Optional
     *                        Whether to include presence data in the output. Setting this to false improves performance, especially with large
     *                        teams.
     * @param callback
     */
    @Throws(UserListingErrorProvider.class)
    @OutputResolver(output = UsersListOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("User - List")
    public void listUsers(@Connection SlackConnection slackConnection,
                          @Optional(defaultValue = "false") boolean includeLocale,
                          @Optional(defaultValue = "0") int limit,
                          @Optional(defaultValue = "false") boolean presence,
                          @Optional String cursor,
                          CompletionCallback<InputStream, Void> callback) {

        slackConnection.user
                .list(cursor, includeLocale, limit, presence)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.members]", "#[payload.response_metadata]", USER_LISTING, callback));
    }

    /**
     * This method returns information about a member of a workspace.
     *
     * @param slackConnection The connection
     * @param user            User to get info on
     * @param includeLocale   Set this to true to receive the locale for this user. Defaults to false
     * @param callback
     */
    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = UsersInfoOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("User - Info")
    public void userInfo(@Connection SlackConnection slackConnection,
                         @Optional @Example("W1234567890 or @john") String user,
                         @Optional(defaultValue = "false") boolean includeLocale,
                         CompletionCallback<InputStream, Void> callback) {
        slackConnection.user
                .info(user, includeLocale)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.user]", USER_LISTING, callback));
    }
}
