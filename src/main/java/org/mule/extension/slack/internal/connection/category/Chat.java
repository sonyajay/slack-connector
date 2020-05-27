package org.mule.extension.slack.internal.connection.category;

import static java.lang.String.valueOf;
import static org.mule.extension.slack.internal.connection.SlackConnection.ifPresent;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHAT_POSTMESSAGE;
import static org.mule.extension.slack.internal.connection.SlackMethods.CHAT_UPDATE;

import org.mule.extension.slack.api.ParsingMode;
import org.mule.extension.slack.internal.MessageConfigurationGroup;
import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class Chat {

    private SlackConnection slackConnection;

    public Chat(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> postMessage(String text,
                                                       String channel,
                                                       InputStream attachments,
                                                       String username,
                                                       MessageConfigurationGroup messageConfig) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("text", text);
        ifPresent(attachments, att -> parameterMap.put("attachments", IOUtils.toString(att)));
        ifPresent(username, user -> parameterMap.put("username", user));
        parameterMap.put("link_names", valueOf(messageConfig.isLinkNames()));
        ifPresent(messageConfig.getParse(), parseMode -> parameterMap.put("parse", valueOf(parseMode)));
        parameterMap.put("reply_broadcast", valueOf(messageConfig.isReplyBroadcast()));
        parameterMap.put("unfurl_links", valueOf(messageConfig.isUnfurlLinks()));
        parameterMap.put("unfurl_media", valueOf(messageConfig.isUnfurlMedia()));
        ifPresent(messageConfig.getIconEmoji(), val -> parameterMap.put("icon_emoji", val));
        ifPresent(messageConfig.getIconUrl(), val -> parameterMap.put("icon_url", val));
        ifPresent(messageConfig.getThreadTimeStamp(), val -> parameterMap.put("thread_ts", val));

        return slackConnection.sendAsyncRequest(API_URI + CHAT_POSTMESSAGE, parameterMap);
    }

    public CompletableFuture<HttpResponse> update(String text, String channel, InputStream attachments, String timestamp, boolean asUser, boolean linkNames, ParsingMode parse) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("channel", channel);
        parameterMap.put("text", text);
        ifPresent(attachments, att -> parameterMap.put("attachments", IOUtils.toString(att)));
        parameterMap.put("ts", timestamp);
        parameterMap.put("link_names", valueOf(linkNames));
        ifPresent(parse, parseMode -> parameterMap.put("parse", valueOf(parseMode)));

        return slackConnection.sendAsyncRequest(API_URI + CHAT_UPDATE, parameterMap);
    }
}
