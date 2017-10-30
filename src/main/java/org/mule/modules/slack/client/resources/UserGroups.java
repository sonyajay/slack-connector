package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.usergroups.Usergroup;

import javax.ws.rs.client.WebTarget;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserGroups {

    private static final String USERGROUP = "usergroup";
    private static final String INCLUDE_DISABLED = "include_disabled";
    private static final String INCLUDE_COUNT = "include_count";
    private static final String INCLUDE_USERS = "include_users";
    private static final String NAME = "name";
    private static final String HANDLE = "handle";
    private static final String DESCRIPTION = "description";
    private static final String CHANNELS = "channels";
    private static final String USERS = "users";
    private final SlackRequester slackRequester;
    private final Gson gson;
    private final Type userGroupsListType = new TypeToken<ArrayList<Usergroup>>() {
    }.getType();
    private final Type stringListType = new TypeToken<List<String>>() {
    }.getType();

    public UserGroups(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public List<Usergroup> listUserGroups(boolean includeDisabled, boolean includeCount, boolean includeUsers) {
        String output = slackRequester.newRequest(Operations.USERGROUPS_LIST)
                .withParam(INCLUDE_DISABLED, (includeDisabled) ? 1 : 0)
                .withParam(INCLUDE_COUNT, (includeCount) ? 1 : 0)
                .withParam(INCLUDE_USERS, (includeUsers) ? 1 : 0)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("usergroups");
        return gson.fromJson(slackResponse.toString(), userGroupsListType);
    }

    public Usergroup enableUserGroup(String usergroupId, boolean includeCount) {
        String output = slackRequester.newRequest(Operations.USERGROUPS_ENABLE)
                .withParam(USERGROUP, usergroupId)
                .withParam(INCLUDE_COUNT, (includeCount) ? 1 : 0)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public Usergroup disableUserGroup(String usergroupId, boolean includeCount) {
        String output = slackRequester.newRequest(Operations.USERGROUPS_DISABLE)
                .withParam(USERGROUP, usergroupId)
                .withParam(INCLUDE_COUNT, (includeCount) ? 1 : 0)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public Usergroup createUserGroup(String name, String handle, String description, List<String> channels, boolean include_count) {
        String output = slackRequester.newRequest(Operations.USERGROUPS_CREATE)
                .withParam(NAME, name)
                .withParam(HANDLE, handle)
                .withParam(DESCRIPTION, description)
                .withParam(CHANNELS, StringUtils.join(channels, ','))
                .withParam(INCLUDE_COUNT, (include_count) ? 1 : 0)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public Usergroup updateUserGroup(String usergroupId, String name, String handle, String description, List<String> channels, boolean include_count) {
        String output = slackRequester.newRequest(Operations.USERGROUPS_UPDATE)
                .withParam(USERGROUP, usergroupId)
                .withParam(NAME, name)
                .withParam(HANDLE, handle)
                .withParam(DESCRIPTION, description)
                .withParam(CHANNELS, StringUtils.join(channels, ','))
                .withParam(INCLUDE_COUNT, (include_count) ? 1 : 0)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public List<String> listUsersFromUserGroup(String usergroupId, boolean includeDisabled) {
        String output = slackRequester.newRequest(Operations.USERGROUPS_USERS_LIST)
                .withParam(USERGROUP, usergroupId)
                .withParam(INCLUDE_DISABLED, (includeDisabled) ? 1 : 0)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERS);
        return gson.fromJson(slackResponse.toString(), stringListType);
    }

    public Usergroup updateUsersFromUserGroup(String usergroupId, List<String> users, boolean includeCount) {
        String output = slackRequester.newRequest(Operations.USERGROUPS_USERS_UPDATE)
                .withParam(USERGROUP, usergroupId)
                .withParam(USERS, StringUtils.join(users, ','))
                .withParam(INCLUDE_COUNT, (includeCount) ? 1 : 0)
                .build().execute();

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }
}
