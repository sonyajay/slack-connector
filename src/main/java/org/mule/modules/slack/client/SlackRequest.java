package org.mule.modules.slack.client;

import java.util.Map;

public abstract class SlackRequest {

    final String token;
    final String apiMethod;
    final Map<String, String> queryParams;
    String SLACK_API_BASE_URI = "https://slack.com/api/";

    SlackRequest(String token, String apiMethod, Map<String, String> queryParams){

        this.token = token;
        this.apiMethod = apiMethod;
        this.queryParams = queryParams;
    }

    abstract public String execute();
}
