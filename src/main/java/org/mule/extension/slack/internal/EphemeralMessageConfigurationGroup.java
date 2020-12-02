package org.mule.extension.slack.internal;

import org.mule.extension.slack.api.ParsingMode;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;

public class EphemeralMessageConfigurationGroup {

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

}
