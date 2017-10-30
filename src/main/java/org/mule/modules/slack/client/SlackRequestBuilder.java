package org.mule.modules.slack.client;

import java.io.InputStream;
import java.util.HashMap;

public abstract class SlackRequestBuilder {

    final HashMap<String, String> queryParams;
    final HashMap<String, InputStream> parts;

    String token;
    String slackMethod;

    SlackRequestBuilder(String token, String slackMethod) {
        this.token = token;
        this.slackMethod = slackMethod;
        this.queryParams = new HashMap<>();
        this.parts = new HashMap<>();
    }

    public SlackRequestBuilder withParam(String key, Object value) {
        if(value != null){
            queryParams.put(key, String.valueOf(value));
        }
        return this;
    }

    public SlackRequestBuilder withBodyPart(String partName, InputStream file) {
        parts.put(partName, file);
        return this;
    }

    public abstract SlackRequest build();


}
