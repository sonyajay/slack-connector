/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client;

public class SlackRequester {

    private String token;
    private RequestBuilderFactory factory;

    public SlackRequester(String token, RequestBuilderFactory factory) {
        this.token = token;
        this.factory = factory;
    }

    public SlackRequestBuilder newRequest(String slackApiMethod) {
        return factory.getBuilder(token, slackApiMethod);
    }
}
