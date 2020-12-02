package org.mule.extension.slack.internal.connection.category;

import static org.mule.extension.slack.internal.connection.SlackConnection.ifPresent;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.REACTIONS_ADD;
import static org.mule.extension.slack.internal.connection.SlackMethods.REACTIONS_GET;
import static org.mule.extension.slack.internal.connection.SlackMethods.REACTIONS_REMOVE;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Reactions {

    private SlackConnection slackConnection;

    public Reactions(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> addReaction(String channel,
                                                       String name,
                                                       String timestamp) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("name", name);
        parameterMap.put("timestamp", timestamp);


        return slackConnection.sendAsyncRequest(API_URI + REACTIONS_ADD, parameterMap);
    }

    public CompletableFuture<HttpResponse> getReaction(String channel,
                                                       String file,
                                                       String fileComment,
                                                       boolean full,
                                                       String timestamp) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        ifPresent(channel, c -> parameterMap.put("channel", c));
        ifPresent(timestamp, f ->  parameterMap.put("timestamp", f));
        ifPresent(file, f ->  parameterMap.put("file", f));
        ifPresent(fileComment, f ->  parameterMap.put("file_comment", f));;
        parameterMap.put("full", String.valueOf(full));


        return slackConnection.sendAsyncRequest(API_URI + REACTIONS_GET, parameterMap);
    }

    public CompletionStage<HttpResponse> removeReaction(String name, String channel, String file, String fileComment, String timestamp) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("name", name);
        ifPresent(channel, c -> parameterMap.put("channel", c));
        ifPresent(timestamp, f ->  parameterMap.put("timestamp", f));
        ifPresent(file, f ->  parameterMap.put("file", f));
        ifPresent(fileComment, f ->  parameterMap.put("file_comment", f));

        return slackConnection.sendAsyncRequest(API_URI + REACTIONS_REMOVE, parameterMap);
    }
}
