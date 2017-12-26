package org.mule.extension.slack.internal.source;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class SlackEventMatcher {

    @Parameter
    @Optional(defaultValue = "NO_ACTION")
    TripleStateBoolean userTyping;

    @Parameter
    @Optional(defaultValue = "NO_ACTION")
    TripleStateBoolean allEvents;

    @Parameter
    @Optional(defaultValue = "NO_ACTION")
    TripleStateBoolean ignoreSelfEvents;

    public TripleStateBoolean getUserTyping() {
        return userTyping;
    }

    public TripleStateBoolean getAllEvents() {
        return allEvents;
    }

    public TripleStateBoolean getIgnoreSelfEvents() {
        return ignoreSelfEvents;
    }
}
