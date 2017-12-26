package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.operations.ChannelOperations;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.util.function.Consumer;

public class ChannelsValueProvider extends BaseValueProvider {

    public ChannelsValueProvider() {
        super("id", "name");
    }

    @Override
    Consumer<CompletionCallback> execute() {
        return completionCallback -> {
            ChannelOperations channelOperations = new ChannelOperations();
            channelOperations.setExpressionManager(expressionManager);
            channelOperations.listChannels(slackConnection, null, true, true, 0, completionCallback);
        };
    }
}
