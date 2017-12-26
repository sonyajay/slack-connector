package org.mule.extension.slack.internal;

import org.mule.extension.slack.api.ParsingMode;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;

public class MessageConfigurationGroup {

    /**
     * Pass true to post the message as the authed user, instead of as a bot. Defaults to false.
     */
    @Optional(defaultValue = "false")
    @Parameter
    boolean asUser;

    /**
     * Set your bot's user name. Must be used in conjunction with as_user set to false, otherwise ignored.
     */
    @Optional
    @DisplayName("Send As")
    @Parameter
    String username;

    /**
     * URL to an image to use as the icon for this message. Must be used in conjunction with as_user set to false,
     * otherwise ignored.
     */
    @Parameter
    @Optional
    @Example("http://lorempixel.com/48/48")
    String iconUrl;

    /**
     * Emoji to use as the icon for this message. Overrides icon_url. Must be used in conjunction with as_user set to
     * false, otherwise ignored.
     */
    @Parameter
    @Optional
    @Example(":chart_with_upwards_trend:")
    String iconEmoji;

    /**
     * Find and link channel names and usernames.
     */
    @Parameter
    @Optional(defaultValue = "false")
    boolean linkNames;

    /**
     * Change how messages are treated. Defaults to none. See: https://api.slack.com/methods/chat.postMessage#formatting
     */
    @Parameter
    @Optional(defaultValue = "NONE")
    ParsingMode parse;

    /**
     * Used in conjunction with Thread Time Stamp and indicates whether reply should be made visible to everyone in the
     * channel or conversation. Defaults to false.
     */
    @Parameter
    @Optional(defaultValue = "false")
    boolean replyBroadcast;

    /**
     * Provide another message's ts value to make this message a reply. Avoid using a reply's ts value; use its parent
     * instead.
     */
    @Example("1234567890.123456")
    @Parameter
    @Optional
    String threadTimeStamp;

    /**
     * Pass true to enable unfurling of primarily text-based content.
     */
    @Parameter
    @Optional(defaultValue = "false")
    boolean unfurlLinks;

    /**
     * Pass false to disable unfurling of media content.
     */
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
