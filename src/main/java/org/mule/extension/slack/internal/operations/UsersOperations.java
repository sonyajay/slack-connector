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
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

@Throws(UsersErrorProvider.class)
public class UsersOperations extends SlackOperations {

    @Throws(UserListingErrorProvider.class)
    @OutputResolver(output = UsersListOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("User - List")
    public void listUsers(@Connection SlackConnection slackConnection,
                          @Optional String cursor,
                          @Optional(defaultValue = "false") boolean includeLocale,
                          @Optional(defaultValue = "0") int limit,
                          @Optional(defaultValue = "false") boolean presence,
                          CompletionCallback<InputStream, Void> callback) {

        slackConnection.user
                .list(cursor, includeLocale, limit, presence)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.members]", "#[payload.response_metadata]", USER_LISTING, callback));
    }

    @Throws(DescribingErrorProvider.class)
    @OutputResolver(output = UsersInfoOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("User - Info")
    public void userInfo(@Connection SlackConnection slackConnection,
                         @Optional String user,
                         @Optional(defaultValue = "false") boolean includeLocale,
                         CompletionCallback<InputStream, Void> callback) {
        slackConnection.user
                .info(user, includeLocale)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.user]", USER_LISTING, callback));
    }
}
