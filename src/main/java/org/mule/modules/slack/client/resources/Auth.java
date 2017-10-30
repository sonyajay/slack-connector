package org.mule.modules.slack.client.resources;

import static org.mule.modules.slack.client.Operations.AUTH_TEST;

import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;

import javax.ws.rs.client.WebTarget;

public class Auth {

    private final SlackRequester slackRequester;

    public Auth(SlackRequester slackRequester) {

        this.slackRequester = slackRequester;
    }

    public String getSelfId() {
        String output = slackRequester.newRequest(AUTH_TEST).build().execute();
        JSONObject slackResponse = new JSONObject(output);
        return slackResponse.getString("user_id");
    }

    public Boolean isConnected() {
        String output = slackRequester.newRequest(AUTH_TEST).build().execute();
        JSONObject slackResponse = new JSONObject(output);
        return slackResponse.getBoolean("ok");
    }

}
