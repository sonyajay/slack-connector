package org.mule.modules.slack.client;

public class JerseySlackRequestBuilder extends SlackRequestBuilder{

    private JerseySlackRequestBuilder(String token, String slackMethod) {
        super(token, slackMethod);
    }

    @Override
    public SlackRequest build() {
        return new JerseySlackRequest(token, slackMethod, queryParams, parts);
    }

    public static RequestBuilderFactory getFactory(){
        return new RequestBuilderFactory() {
            @Override
            public SlackRequestBuilder getBuilder(String token, String slackMethod) {
                return new JerseySlackRequestBuilder(token, slackMethod);
            }
        };
    }
}
