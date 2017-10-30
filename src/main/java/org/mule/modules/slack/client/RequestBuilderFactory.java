package org.mule.modules.slack.client;

public interface RequestBuilderFactory {

    SlackRequestBuilder getBuilder(String token, String slackMethod);

}
