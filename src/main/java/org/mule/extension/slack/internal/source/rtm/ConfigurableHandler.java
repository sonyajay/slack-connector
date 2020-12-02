/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.slack.internal.source.rtm;

import static org.mule.runtime.api.metadata.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.source.rtm.filter.EventFilter;
import org.mule.extension.slack.internal.source.rtm.filter.EventNotifier;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurableHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurableHandler.class);
    private SourceCallback sourceCallback;
    private Gson gson;
    private Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    private List<EventNotifier> observerList;
    private List<EventFilter> eventFilterList;

    public ConfigurableHandler(SourceCallback sourceCallback, List<EventNotifier> eventNotifierList, List<EventFilter> eventFilterList) {
        this.sourceCallback = sourceCallback;
        gson = new Gson();
        this.observerList = eventNotifierList;
        this.eventFilterList = eventFilterList;
    }

    public void onMessage(String message) {
        logger.debug(message);
        Map<String, Object> messageMap = gson.fromJson(message, type);

        if (shouldBeAccepted(messageMap, eventFilterList)) {
            if (shouldBeSent(messageMap, observerList)) {
                try {
                    sourceCallback.handle(Result.builder().output(message).mediaType(APPLICATION_JSON).build());
                } catch (Exception e) {
                    logger.error("Error", e);
                }
            }
        }
    }

    private boolean shouldBeAccepted(Map<String, Object> message, List<EventFilter> filterList) {
        for (EventFilter eventFilter : filterList) {
            if (!eventFilter.shouldAccept(message)) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldBeSent(Map<String, Object> message, List<EventNotifier> observerList) {
        for (EventNotifier eventNotifier : observerList) {
            if (eventNotifier.shouldSend(message)) {
                return true;
            }
        }
        return false;
    }
}
