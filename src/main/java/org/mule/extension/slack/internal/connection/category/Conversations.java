package org.mule.extension.slack.internal.connection.category;

import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_ARCHIVE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_CLOSE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_CREATE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_HISTORY;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_INFO;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_INVITE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_JOIN;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_KICK;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_LEAVE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_LIST;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_MARK;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_MEMBERS;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_OPEN;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_RENAME;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_REPLIES;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_SETPURPOSE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_SETTOPIC;
import static org.mule.extension.slack.internal.connection.SlackMethods.CONVERSATIONS_UNARCHIVE;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.utils.ParameterMapBuilder;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

public final class Conversations {
    SlackConnection slackConnection;

    public Conversations(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> archive(String channel) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_ARCHIVE, build);
    }

    public CompletableFuture<HttpResponse> close(String channel) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_CLOSE, build);
    }

    public CompletableFuture<HttpResponse> create(String name, Boolean is_private) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("name", name)  // Optional - string
                .addNullable("is_private", is_private)  // Optional - boolean
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_CREATE, build);
    }

    public CompletableFuture<HttpResponse> history(String channel, String latest, String oldest,
                                                   Boolean inclusive, Integer limit, String cursor) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("latest", latest)  // Optional - number
                .addNullable("oldest", oldest)  // Optional - number
                .addNullable("inclusive", inclusive)  // Optional - boolean
                .addNullable("limit", limit)  // Optional - integer
                .addNullable("cursor", cursor)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_HISTORY, build);
    }

    public CompletableFuture<HttpResponse> info(String channel, Boolean include_locale,
                                                Boolean include_num_members) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("include_locale", include_locale)  // Optional - boolean
                .addNullable("include_num_members", include_num_members)  // Optional - boolean
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_INFO, build);
    }

    public CompletableFuture<HttpResponse> invite(String channel, String users) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("users", users)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_INVITE, build);
    }

    public CompletableFuture<HttpResponse> join(String channel) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_JOIN, build);
    }

    public CompletableFuture<HttpResponse> kick(String channel, String user) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("user", user)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_KICK, build);
    }

    public CompletableFuture<HttpResponse> leave(String channel) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_LEAVE, build);
    }

    public CompletableFuture<HttpResponse> list(Boolean exclude_archived, String types, Integer limit,
                                                String cursor) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("exclude_archived", exclude_archived)  // Optional - boolean
                .addNullable("types", types)  // Optional - string
                .addNullable("limit", limit)  // Optional - integer
                .addNullable("cursor", cursor)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_LIST, build);
    }

    public CompletableFuture<HttpResponse> mark(String channel, String ts) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("ts", ts)  // Optional - number
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_MARK, build);
    }

    public CompletableFuture<HttpResponse> members(String channel, Integer limit, String cursor) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("limit", limit)  // Optional - integer
                .addNullable("cursor", cursor)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_MEMBERS, build);
    }

    public CompletableFuture<HttpResponse> open(String channel, String users, Boolean return_im) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("users", users)  // Optional - string
                .addNullable("return_im", return_im)  // Optional - boolean
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_OPEN, build);
    }

    public CompletableFuture<HttpResponse> rename(String channel, String name) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("name", name)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_RENAME, build);
    }

    public CompletableFuture<HttpResponse> replies(String channel, String ts, String latest,
                                                   String oldest, Boolean inclusive, Integer limit, String cursor) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("ts", ts)  // Optional - number
                .addNullable("latest", latest)  // Optional - number
                .addNullable("oldest", oldest)  // Optional - number
                .addNullable("inclusive", inclusive)  // Optional - boolean
                .addNullable("limit", limit)  // Optional - integer
                .addNullable("cursor", cursor)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_REPLIES, build);
    }

    public CompletableFuture<HttpResponse> setPurpose(String channel, String purpose) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("purpose", purpose)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_SETPURPOSE, build);
    }

    public CompletableFuture<HttpResponse> setTopic(String channel, String topic) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .addNullable("topic", topic)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_SETTOPIC, build);
    }

    public CompletableFuture<HttpResponse> unarchive(String channel) {
        MultiMap<String,String> build = new ParameterMapBuilder()
                .addNullable("channel", channel)  // Optional - string
                .build();
        return slackConnection.sendAsyncRequest(API_URI + CONVERSATIONS_UNARCHIVE, build);
    }
}
