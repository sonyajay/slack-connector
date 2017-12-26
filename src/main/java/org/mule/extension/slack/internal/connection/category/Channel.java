package org.mule.extension.slack.internal.connection.category;

import static java.lang.String.valueOf;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHANNELS_INFO;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHANNELS_INVITE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHANNELS_LIST;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHANNELS_RENAME;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHANNELS_SETPURPOSE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHANNELS_SETTOPIC;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Channel {

    private SlackConnection slackConnection;

    public Channel(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletionStage<HttpResponse> info(String channel, boolean includeLocale) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("include_locale", valueOf(includeLocale));
        return slackConnection.sendAsyncRequest(API_URI + CHANNELS_INFO, parameterMap);
    }

    public CompletionStage<HttpResponse> setPurpose(String channel, String purpose) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("purpose", purpose);
        return slackConnection.sendAsyncRequest(API_URI + CHANNELS_SETPURPOSE, parameterMap);
    }

    public CompletionStage<HttpResponse> setTopic(String channel, String topic) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("topic", topic);
        return slackConnection.sendAsyncRequest(API_URI + CHANNELS_SETTOPIC, parameterMap);
    }

    public CompletionStage<HttpResponse> rename(String channel, String name, boolean validate) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("name", name);
        parameterMap.put("validate", valueOf(validate));
        return slackConnection.sendAsyncRequest(API_URI + CHANNELS_RENAME, parameterMap);
    }

    public CompletionStage<HttpResponse> invite(String channel, String user) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("user", user);
        return slackConnection.sendAsyncRequest(API_URI + CHANNELS_INVITE, parameterMap);
    }

    public CompletableFuture<HttpResponse> list(String cursor, boolean excludeArchived, boolean excludeMembers, int limit) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("cursor", cursor);
        parameterMap.put("exclude_archived", valueOf(excludeArchived));
        parameterMap.put("exclude_members", valueOf(excludeMembers));
        parameterMap.put("limit", valueOf(limit));
        return slackConnection.sendAsyncRequest(API_URI + CHANNELS_LIST, parameterMap);
    }
}
