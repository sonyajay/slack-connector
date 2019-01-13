package org.mule.extension.slack.internal.source;

import org.mule.extension.slack.internal.valueprovider.RTMChannelValueProvider;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;

public class SlackMessageEventMatcher {

    private static final String SPACE = " ";
    private static final String EMPTY = "";

    @Parameter
    @Optional
    @OfValues(RTMChannelValueProvider.class)
    @DisplayName("Only from channel")
    private String channel;

    @Parameter
    @Optional(defaultValue = "false")
    @DisplayName("Only Direct Messages (DM)")
    private boolean onlyDmMessages;

    @Parameter
    @Optional(defaultValue = "true")
    @DisplayName("Only New Messages")
    private boolean onlyNewMessages;

    //    @Parameter
//    @Optional(defaultValue = "NO_ACTION")
//    private TripleStateBoolean messageSubTypesAction;
//
//    @Parameter
//    @Optional
//    private String messageSubTypes = "";
//
//    private LazyValue<List<String>> subTypes = new LazyValue<>(() -> of(messageSubTypes.split(","))
//            .map(str -> str.replace(SPACE, EMPTY))
//            .collect(toList()));
//
    public boolean isOnlyDmMessages() {
        return onlyDmMessages;
    }

    public boolean isOnlyNewMessages() {
        return onlyNewMessages;
    }

    public String getChannel() {
        return channel;
    }

    //    public List<String> getSubTypes() {
//        return subTypes.get();
//    }
//
//    public TripleStateBoolean getSubTypesAction() {
//        return messageSubTypesAction;
//    }
}
