package org.mule.modules.slack.client.resources;

import static org.mule.modules.slack.client.Operations.GROUPS_ARCHIVE;
import static org.mule.modules.slack.client.Operations.GROUPS_CLOSE;
import static org.mule.modules.slack.client.Operations.GROUPS_CREATE;
import static org.mule.modules.slack.client.Operations.GROUPS_HISTORY;
import static org.mule.modules.slack.client.Operations.GROUPS_INFO;
import static org.mule.modules.slack.client.Operations.GROUPS_LEAVE;
import static org.mule.modules.slack.client.Operations.GROUPS_LIST;
import static org.mule.modules.slack.client.Operations.GROUPS_MARK;
import static org.mule.modules.slack.client.Operations.GROUPS_OPEN;
import static org.mule.modules.slack.client.Operations.GROUPS_SETPORPUSE;
import static org.mule.modules.slack.client.Operations.GROUPS_SETTOPIC;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.client.model.group.Group;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Groups {

    private final Type groupsListType = new TypeToken<List<Group>>() {
    }.getType();
    private final SlackRequester slackRequester;
    private final Gson gson;

    public Groups(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public List<Message> getGroupHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, GROUPS_HISTORY);
    }

    public List<Group> getGroupList(Boolean excludeMembers, Boolean excludeArchived) {
        String output = slackRequester.newRequest(GROUPS_LIST)
                .withParam("exclude_members", excludeMembers)
                .withParam("exclude_archived", excludeArchived)
                .build().execute();

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("groups");
        return gson.fromJson(slackResponse.toString(), groupsListType);
    }

    public Group createGroup(String name) {
        String output = slackRequester.newRequest(GROUPS_CREATE)
                .withParam("name", name)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("group");
        return gson.fromJson(slackResponse.toString(), Group.class);
    }

    public Boolean openGroup(String channelID) {
        String output = slackRequester.newRequest(GROUPS_OPEN)
                .withParam("channel", channelID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean leaveGroup(String channelID) {
        String output = slackRequester.newRequest(GROUPS_LEAVE)
                .withParam("channel", channelID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean archiveGroup(String channelID) {
        String output = slackRequester.newRequest(GROUPS_ARCHIVE)
                .withParam("channel", channelID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean setGroupPurpose(String channelID, String purpose) {
        String output = slackRequester.newRequest(GROUPS_SETPORPUSE)
                .withParam("channel", channelID)
                .withParam("purpose", purpose)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean setGroupTopic(String channelID, String topic) {
        String output = slackRequester.newRequest(GROUPS_SETTOPIC)
                .withParam("channel", channelID)
                .withParam("topic", topic)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean closeGroup(String channelID) {
        String output = slackRequester.newRequest(GROUPS_CLOSE)
                .withParam("channel", channelID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean markViewGroup(String channelID, String timeStamp) {
        String output = slackRequester.newRequest(GROUPS_MARK)
                .withParam("channel", channelID)
                .withParam("ts", timeStamp)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean kickUserFromGroup(String channelID, String user) {
        String output = slackRequester.newRequest(Operations.GROUPS_KICK)
                .withParam("channel", channelID)
                .withParam("user", user)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean inviteUserToGroup(String channelID, String user) {
        String output = slackRequester.newRequest(Operations.GROUPS_INVITE)
                .withParam("channel", channelID)
                .withParam("user", user)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean unarchiveGroup(String groupID) {
        String output = slackRequester.newRequest(Operations.GROUPS_UNARCHIVE)
                .withParam("channel", groupID)
                .build().execute();

        return new JSONObject(output).getBoolean("ok");
    }

    public Group renameGroup(String channelId, String newName) {
        String output = slackRequester.newRequest(Operations.GROUPS_RENAME)
                .withParam("channel", channelId)
                .withParam("name", newName)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return gson.fromJson(slackResponse.toString(), Group.class);
    }

    public Group getGroupInfo(String groupId) {
        String output = slackRequester.newRequest(GROUPS_INFO)
                .withParam("channel", groupId)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("group");
        return gson.fromJson(slackResponse.toString(), Group.class);
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
