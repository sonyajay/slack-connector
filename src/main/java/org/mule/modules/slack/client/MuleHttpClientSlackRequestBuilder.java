package org.mule.modules.slack.client;

import org.mule.module.http.internal.request.HttpClient;

public class MuleHttpClientSlackRequestBuilder extends SlackRequestBuilder {

    private final HttpClient httpClient;

    private MuleHttpClientSlackRequestBuilder(String token, String slackMethod, HttpClient httpClient) {
        super(token, slackMethod);
        this.httpClient = httpClient;
    }

    @Override
    public SlackRequest build() {
        return new MuleHttpClientSlackRequest(token, slackMethod, queryParams, parts, httpClient);
    }

    public static RequestBuilderFactory getFactory(final HttpClient httpClient){
        return new RequestBuilderFactory() {
            @Override
            public SlackRequestBuilder getBuilder(String token, String slackMethod) {
                return new MuleHttpClientSlackRequestBuilder(token, slackMethod, httpClient);
            }
        };
    }
}
