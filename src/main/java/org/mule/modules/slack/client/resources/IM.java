package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.client.model.im.DirectMessageChannel;
import org.mule.modules.slack.client.model.im.DirectMessageChannelCreationResponse;

import javax.ws.rs.client.WebTarget;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IM {

    private final Type channelListType = new TypeToken<List<DirectMessageChannel>>() {
    }.getType();
    private final SlackRequester slackRequester;
    private final Gson gson;

    public IM(SlackRequester slackRequester, Gson gson) {
        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public DirectMessageChannelCreationResponse openDirectMessageChannel(String userId) {
        String output = slackRequester.newRequest(Operations.IM_OPEN)
                .withParam("user", userId)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return gson.fromJson(slackResponse.toString(), DirectMessageChannelCreationResponse.class);
    }

    public List<DirectMessageChannel> getDirectMessageChannelsList() {
        String output = slackRequester.newRequest(Operations.IM_LIST)
                .build().execute();

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("ims");
        return gson.fromJson(slackResponse.toString(), channelListType);
    }

    public List<Message> getDirectChannelHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, Operations.IM_HISTORY);
    }

    public Boolean markViewDirectMessageChannel(String channelID, String timeStamp) {
        String output = slackRequester.newRequest(Operations.IM_MARK)
                .withParam("channel", channelID)
                .withParam("ts", timeStamp)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean closeDirectMessageChannel(String channelID) {
        String output = slackRequester.newRequest(Operations.IM_CLOSE)
                .withParam("channel", channelID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public List<Message> getMessages(String channelId, String latest, String oldest, String count, String operation) {
        String output = slackRequester.newRequest(operation)
                .withParam("channel", channelId)
                .withParam("latest", latest)
                .withParam("oldest", oldest)
                .withParam("count", count)
                .build().execute();

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("messages");
        Type listType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        return gson.fromJson(slackResponse.toString(), listType);
    }
}
