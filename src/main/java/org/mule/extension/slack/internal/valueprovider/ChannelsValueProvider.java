package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.ChannelOperations;
import org.mule.extension.slack.internal.utils.PagingProviderIterator;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;

public class ChannelsValueProvider extends BaseValueProvider {

    public ChannelsValueProvider() {
        super("id", "name");
    }

    public ChannelsValueProvider(ExpressionManager expressionManager, SlackConnection slackConnection) {
        super("id", "name", expressionManager, slackConnection);
    }

    @Override
    PagingProvider<SlackConnection, Map<String, Object>> getPagingProvider() {
        ChannelOperations channelOperations = new ChannelOperations();
        channelOperations.setExpressionManager(expressionManager);
        return channelOperations.listChannels(true, true, 0);
    }
}
