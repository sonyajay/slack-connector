package org.mule.modules.slack.client.resources;

import static org.apache.commons.httpclient.util.URIUtil.encodeAll;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.glassfish.jersey.uri.UriComponent.Type.QUERY_PARAM_SPACE_ENCODED;
import static org.glassfish.jersey.uri.UriComponent.encode;
import static org.mule.modules.slack.client.Operations.CHAT_DELETE;
import static org.mule.modules.slack.client.Operations.CHAT_POSTMESSAGE;
import static org.mule.modules.slack.client.Operations.CHAT_UPDATE;

import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.util.StringUtils;

import com.google.gson.Gson;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.glassfish.jersey.uri.UriComponent;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.client.WebTarget;

public class Chat {

    private final SlackRequester slackRequester;
    private final Gson gson;

    public Chat(SlackRequester slackRequester, Gson gson) {
        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public MessageResponse sendMessage(String message, String channelId, String username, String iconUrl, Boolean asUser) {
        String output = slackRequester.newRequest(CHAT_POSTMESSAGE)
                .withParam("channel", channelId)
                .withParam("text", message)
                .withParam("username", username)
                .withParam("icon_url", iconUrl)
                .withParam("as_user", asUser)
                .build().execute();

        return gson.fromJson(output, MessageResponse.class);
    }

    public String sendMessageWithAttachments(String message, String channelId, String username, String iconUrl, String attachments, Boolean asUser) {
        return slackRequester.newRequest(CHAT_POSTMESSAGE)
                .withParam("channel", channelId)
                .withParam("text", message)
                .withParam("username", username)
                .withParam("icon_url", iconUrl)
                .withParam("as_user", asUser)
                .withParam("attachments", attachments)
                .build().execute();
    }

    public Boolean deleteMessage(String timeStamp, String channelId) {
        String output = slackRequester.newRequest(CHAT_DELETE)
                .withParam("channel", channelId)
                .withParam("ts", timeStamp)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean updateMessage(String timeStamp, String channelId, String message) {
        String output = slackRequester.newRequest(CHAT_UPDATE)
                .withParam("channel", channelId)
                .withParam("text", message)
                .withParam("ts", timeStamp)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    private String encode(String text) {
        try {
            if(!isBlank(text)){
                text = text.replace("\\\\n", "\n");
                return encodeAll(text, "UTF-8");
            } else {
                return text;
            }
        } catch (URIException e) {
            throw new RuntimeException(e);
        }
    }
}
