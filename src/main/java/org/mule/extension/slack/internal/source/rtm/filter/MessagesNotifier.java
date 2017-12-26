/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.slack.internal.source.rtm.filter;

import static java.util.Optional.ofNullable;

import org.mule.extension.slack.internal.source.SlackMessageEventMatcher;

import java.util.Map;
import java.util.Optional;

public class MessagesNotifier implements EventNotifier {

    private SlackMessageEventMatcher messageEventMatcher;

    public MessagesNotifier(SlackMessageEventMatcher messageEventMatcher) {

        this.messageEventMatcher = messageEventMatcher;
    }

    @Override
    public boolean shouldSend(Map<String, Object> message) {
        if (messageEventMatcher.getChannel() != null && !message.get("channel").toString().equals(messageEventMatcher.getChannel())) {
            return false;
        }

        if (!message.get("type").equals("message")) {
            return false;
        }

        if (messageEventMatcher.isOnlyNewMessages() && !isNewMessage(message)) {
            return false;
        }

        if (messageEventMatcher.isOnlyDmMessages() && !((String) message.get("channel")).toLowerCase().startsWith("d")) {
            return false;
        }

//            if (!messageEventMatcher.getSubTypesAction().equals(NO_ACTION)) {
//                Optional<String> subtype = getSubtype(message);
//                if (subtype.isPresent()) {
//                    if (messageEventMatcher.getSubTypes().contains(subtype.get())) {
//                        returnValue = messageEventMatcher.getSubTypesAction().equals(ACCEPT);
//                    }
//                }
//            }
        return true;
    }

    private Boolean isNewMessage(Map<String, Object> message) {
        return !getSubtype(message).isPresent();
    }

    private Optional<String> getSubtype(Map<String, Object> message) {
        return ofNullable((String) message.get("subtype"));
    }
}
