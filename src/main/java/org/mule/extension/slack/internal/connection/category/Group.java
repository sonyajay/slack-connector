package org.mule.extension.slack.internal.connection.category;

import static java.lang.String.valueOf;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.GROUPS_INFO;
import static org.mule.extension.slack.internal.connection.SlackMethods.GROUPS_LIST;
import static org.mule.extension.slack.internal.connection.SlackMethods.GROUPS_RENAME;
import static org.mule.extension.slack.internal.connection.SlackMethods.GROUPS_SETPORPUSE;
import static org.mule.extension.slack.internal.connection.SlackMethods.GROUPS_SETTOPIC;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

public class Group {

    private SlackConnection slackConnection;

    public Group(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> list(boolean excludeArchived, boolean excludeMembers) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("exclude_archived", valueOf(excludeArchived));
        parameterMap.put("exclude_members", valueOf(excludeMembers));
        return slackConnection.sendAsyncRequest(API_URI + GROUPS_LIST, parameterMap);
    }

    public CompletableFuture<HttpResponse> info(String channel, boolean includeLocale) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("include_locale", valueOf(includeLocale));
        return slackConnection.sendAsyncRequest(API_URI + GROUPS_INFO, parameterMap);
    }

    public CompletableFuture<HttpResponse> setTopic(String channel, String topic) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("topic", topic);
        return slackConnection.sendAsyncRequest(API_URI + GROUPS_SETTOPIC, parameterMap);
    }

    public CompletableFuture<HttpResponse> setPurpose(String channel, String purpose) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("purpose", purpose);
        return slackConnection.sendAsyncRequest(API_URI + GROUPS_SETPORPUSE, parameterMap);
    }

    public CompletableFuture<HttpResponse> rename(String channel, String name, boolean validate) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("name", name);
        parameterMap.put("validate", valueOf(validate));
        return slackConnection.sendAsyncRequest(API_URI + GROUPS_RENAME, parameterMap);
    }

}
