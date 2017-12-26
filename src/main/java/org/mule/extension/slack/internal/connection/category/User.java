package org.mule.extension.slack.internal.connection.category;

import static java.lang.String.valueOf;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.USER_INFO;
import static org.mule.extension.slack.internal.connection.SlackMethods.USER_LIST;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

public class User {

    private SlackConnection slackConnection;

    public User(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> list(String cursor, boolean includeLocale, int limit, boolean presence) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("cursor", cursor);
        parameterMap.put("limit", valueOf(limit));
        parameterMap.put("include_locale", valueOf(includeLocale));
        parameterMap.put("presence", valueOf(presence));
        return slackConnection.sendAsyncRequest(API_URI + USER_LIST, parameterMap);
    }

    public CompletableFuture<HttpResponse> info(String user, boolean includeLocale) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("user", user);
        parameterMap.put("include_locale", valueOf(includeLocale));
        return slackConnection.sendAsyncRequest(API_URI + USER_INFO, parameterMap);
    }
}
