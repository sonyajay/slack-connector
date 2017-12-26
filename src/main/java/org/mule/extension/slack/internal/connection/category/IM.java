package org.mule.extension.slack.internal.connection.category;

import static java.lang.String.valueOf;
import static org.mule.extension.slack.internal.connection.SlackConnection.ifPresent;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.IM_LIST;
import static org.mule.extension.slack.internal.connection.SlackMethods.IM_OPEN;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

public class IM {

    private SlackConnection slackConnection;

    public IM(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> list(String cursor, Integer limit) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        ifPresent(cursor, val -> parameterMap.put("cursor", val));
        ifPresent(limit, val -> parameterMap.put("limit", valueOf(val)));
        return slackConnection.sendAsyncRequest(API_URI + IM_LIST, parameterMap);
    }

    public CompletableFuture<HttpResponse> open(String user, boolean includeLocale, boolean returnIm) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("user", user);
        parameterMap.put("include_locale", valueOf(includeLocale));
        parameterMap.put("return_im", valueOf(returnIm));
        return slackConnection.sendAsyncRequest(API_URI + IM_OPEN, parameterMap);
    }
}
