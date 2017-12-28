package org.mule.extension.slack.internal.connection.category;

import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.AUTH_TEST;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

public class Auth {
    private SlackConnection slackConnection;

    public Auth(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> test() {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        return slackConnection.sendAsyncRequest(API_URI + AUTH_TEST, parameterMap);
    }
}
