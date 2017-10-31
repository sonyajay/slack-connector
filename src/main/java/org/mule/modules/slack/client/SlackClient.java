/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client;

import static java.lang.String.format;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.mule.modules.slack.client.resources.*;
import org.mule.modules.slack.client.rtm.EventHandler;
import org.mule.modules.slack.client.rtm.SlackMessageHandler;

import javax.websocket.DeploymentException;

import java.io.IOException;

public class SlackClient {

    private static final Logger logger = Logger.getLogger(SlackClient.class);
    private SlackRequester slackRequester;
    private String selfId;
    public final Chat chat;
    public final Users users;
    public final UserGroups usergroups;
    public final Channels channels;
    public final Groups groups;
    public final Files files;
    public final Auth auth;
    public final IM im;

    public SlackClient(String token) {
        this(token, JerseySlackRequestBuilder.getFactory());
    }

    public SlackClient(String token, RequestBuilderFactory requestBuilderFactory) {
        slackRequester = new SlackRequester(token, requestBuilderFactory);

        Gson gson = new Gson();
        usergroups = new UserGroups(slackRequester, gson);
        channels = new Channels(slackRequester, gson);
        users = new Users(slackRequester, gson);
        groups = new Groups(slackRequester, gson);
        chat = new Chat(slackRequester, gson);
        auth = new Auth(slackRequester);
        im = new IM(slackRequester, gson);
        files = new Files(slackRequester, gson);
    }

    // ******************
    // RTM
    // ******************

    private String getWebSockerURI() {
        String output = slackRequester.newRequest(Operations.RTM_START).build().execute();
        selfId = new JSONObject(output).getJSONObject("self").getString("id");
        return new JSONObject(output).getString("url");
    }

    public void startRealTimeCommunication(EventHandler messageHandler, int reconnectionFrequency) throws DeploymentException, InterruptedException, IOException {
        SlackMessageHandler slackMessageHandler = new SlackMessageHandler(this.getWebSockerURI(), messageHandler);
        while (true) {
            try {
                slackMessageHandler.connect();
            } catch (InterruptedException e){
                logger.warn("The event listener has been interrupted and stopped definitely");
            } catch (Exception e) {
                logger.error("Error Cause: ", e);
                logger.warn(format("Retrying RTM Communication in %s Seconds", reconnectionFrequency / 1000));
                Thread.sleep(reconnectionFrequency);
                logger.warn("Starting RTM Communication");
                slackMessageHandler = new SlackMessageHandler(this.getWebSockerURI(), messageHandler);
            }
        }
    }
}
