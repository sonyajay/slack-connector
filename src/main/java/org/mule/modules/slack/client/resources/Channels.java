package org.mule.modules.slack.client.resources;

import static org.mule.modules.slack.client.Operations.CHANNELS_ARCHIVE;
import static org.mule.modules.slack.client.Operations.CHANNELS_CREATE;
import static org.mule.modules.slack.client.Operations.CHANNELS_HISTORY;
import static org.mule.modules.slack.client.Operations.CHANNELS_INVITE;
import static org.mule.modules.slack.client.Operations.CHANNELS_KICK;
import static org.mule.modules.slack.client.Operations.CHANNELS_LEAVE;
import static org.mule.modules.slack.client.Operations.CHANNELS_LIST;
import static org.mule.modules.slack.client.Operations.CHANNELS_MARK;
import static org.mule.modules.slack.client.Operations.CHANNELS_RENAME;
import static org.mule.modules.slack.client.Operations.CHANNELS_SETPURPOSE;
import static org.mule.modules.slack.client.Operations.CHANNELS_SETTOPIC;
import static org.mule.modules.slack.client.Operations.CHANNELS_UNARCHIVE;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.chat.Message;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Channels {

    protected static final String CHANNEL = "channel";
    private final SlackRequester slackRequester;
    private final Gson gson;

    public Channels(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public List<Channel> getChannelList(Boolean excludeArchived, Boolean excludeMembers, int limit, String cursor) {
        List<Channel> list = new ArrayList<Channel>();
        String output = slackRequester.newRequest(CHANNELS_LIST)
                .withParam("exclude_archived", excludeArchived)
                .withParam("exclude_members", excludeMembers)
                .withParam("limit", limit)
                .withParam("cursor", cursor)
                .build().execute();
        JSONArray channels = (JSONArray) new JSONObject(output).get("channels");

        for (int i = 0; i < channels.length(); i++) {
            JSONObject channel = (JSONObject) channels.get(i);
            Channel newChannel = gson.fromJson(channel.toString(), Channel.class);
            list.add(newChannel);
        }
        return list;
    }

    public Boolean leaveChannel(String channelId) {
        String output = slackRequester
                .newRequest(CHANNELS_LEAVE)
                .withParam(CHANNEL, channelId)
                .build()
                .execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Channel getChannelById(String id) {
        String output = slackRequester.newRequest(Operations.CHANNELS_INFO).withParam(CHANNEL, id).build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(CHANNEL);
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public List<Message> getChannelHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, CHANNELS_HISTORY);
    }

    public Channel createChannel(String channelName) {
        String output = slackRequester.newRequest(CHANNELS_CREATE).withParam("name", channelName).build().execute();
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(CHANNEL);
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel renameChannel(String channelId, String newName) {
        String output = slackRequester.newRequest(CHANNELS_RENAME).withParam("name", newName).withParam(CHANNEL, channelId).build().execute();
        

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(CHANNEL);
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel joinChannel(String channelName) {
        String output = slackRequester.newRequest(Operations.CHANNELS_JOIN).withParam("name", channelName).build().execute();
        

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(CHANNEL);
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel getChannelByName(String name) {
        List<Channel> list = getChannelList(false, true, 0, null);
        for (Channel channel : list) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }
        throw new SlackException("Channel: " + name + " does not exist.");
    }

    public Boolean setChannelPurpose(String channelID, String purpose) {
        String output = slackRequester.newRequest(CHANNELS_SETPURPOSE)
                .withParam(CHANNEL, channelID)
                .withParam("purpose", purpose)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean setChannelTopic(String channelID, String topic) {
        String output = slackRequester.newRequest(CHANNELS_SETTOPIC)
                .withParam(CHANNEL, channelID)
                .withParam("topic", topic)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean markViewChannel(String channelID, String timeStamp) {
        String output = slackRequester.newRequest(CHANNELS_MARK)
                .withParam(CHANNEL, channelID)
                .withParam("ts", timeStamp)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean kickUserFromChannel(String channelID, String user) {
        String output = slackRequester.newRequest(CHANNELS_KICK)
                .withParam(CHANNEL, channelID)
                .withParam("user", user)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean inviteUserToChannel(String channelID, String user) {
        String output = slackRequester.newRequest(CHANNELS_INVITE)
                .withParam(CHANNEL, channelID
                ).withParam("user", user)
                .build().execute();
        
        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean unarchiveChannel(String channelID) {
        String output = slackRequester
                .newRequest(CHANNELS_UNARCHIVE)
                .withParam(CHANNEL, channelID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean archiveChannel(String channelID) {
        String output = slackRequester
                .newRequest(CHANNELS_ARCHIVE)
                .withParam(CHANNEL, channelID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public List<Message> getMessages(String channelId, String latest, String oldest, String count, String operation) {
        String output = slackRequester.newRequest(operation)
                .withParam(CHANNEL, channelId)
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
