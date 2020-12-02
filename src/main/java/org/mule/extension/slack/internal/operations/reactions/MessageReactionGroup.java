package org.mule.extension.slack.internal.operations.reactions;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Example;

public class MessageReactionGroup {

    @Parameter
    @Example("C1234567890")
    String channel;

    @Parameter
    @Example("1405894322.002768")
    String timestamp;

    public String getChannel() {
        return channel;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
