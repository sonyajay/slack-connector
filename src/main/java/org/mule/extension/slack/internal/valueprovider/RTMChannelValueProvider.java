package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class RTMChannelValueProvider implements ValueProvider {

    @Connection
    SlackConnection slackConnection;

    @Inject
    ExpressionManager expressionManager;

    @Override
    public Set<Value> resolve() throws ValueResolvingException {
        ConversationsValueProvider conversationsValueProvider = new ConversationsValueProvider(expressionManager, slackConnection);
        conversationsValueProvider.setIm(true);
        conversationsValueProvider.setMpim(false);
        conversationsValueProvider.setPublicChannels(true);
        conversationsValueProvider.setPrivateChannels(true);
        return conversationsValueProvider.resolve();
    }
}
