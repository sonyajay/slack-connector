package org.mule.extension.slack.internal;

import org.mule.extension.slack.api.ParsingMode;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class MessageConfigurationGroup {

    /**
     * Pass true to post the message as the authed user, instead of as a bot. Defaults to false.
     */
    @Optional(defaultValue = "false")
    @Parameter
    boolean asUser;

    @Optional
    @DisplayName("Send As")
    @Parameter
    String username;

    @Parameter
    @Optional
    String iconUrl;

    @Parameter
    @Optional
    String iconEmoji;

    @Parameter
    @Optional(defaultValue = "false")
    boolean linkNames;

    @Parameter
    @Optional(defaultValue = "NONE")
    ParsingMode parse;

    @Parameter
    @Optional(defaultValue = "false")
    boolean replyBroadcast;

    @Parameter
    @Optional
    String threadTimeStamp;

    @Parameter
    @Optional(defaultValue = "false")
    boolean unfurlLinks;

    @Parameter
    @Optional(defaultValue = "false")
    boolean unfurlMedia;

    public boolean isAsUser() {
        return asUser;
    }

    public String getUsername() {
        return username;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getIconEmoji() {
        return iconEmoji;
    }

    public boolean isLinkNames() {
        return linkNames;
    }

    public ParsingMode getParse() {
        return parse;
    }

    public boolean isReplyBroadcast() {
        return replyBroadcast;
    }

    public String getThreadTimeStamp() {
        return threadTimeStamp;
    }

    public boolean isUnfurlLinks() {
        return unfurlLinks;
    }

    public boolean isUnfurlMedia() {
        return unfurlMedia;
    }
}
