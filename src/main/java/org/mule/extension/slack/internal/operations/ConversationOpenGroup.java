package org.mule.extension.slack.internal.operations;

import org.mule.runtime.extension.api.annotation.param.ExclusiveOptionals;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import java.util.List;

@ExclusiveOptionals(isOneRequired = true)
public class ConversationOpenGroup {

    @Optional
    @Parameter
    @DisplayName("from Channel")
    private String channel;

    @Optional
    @Parameter
    @DisplayName("with Users")
    private List<String> users;

    public String getChannel() {
        return channel;
    }

    public String getUsers() {
        return String.join(",", users);
    }
}
