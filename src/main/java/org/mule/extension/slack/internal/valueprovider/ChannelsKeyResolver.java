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

//TODO - Si esto se convierte a un KeysResolver se mezclan los resolvers
public class ChannelsKeyResolver implements ValueProvider {

    @Connection
    SlackConnection slackConnection;

    @Inject
    ExpressionManager expressionManager;

    @Override
    public Set<Value> resolve() throws ValueResolvingException {
        Set<Value> allChannels = new HashSet<>();

        Set<Value> channels = new ChannelsValueProvider(expressionManager, slackConnection).resolve();
        Set<Value> groups = new GroupsValueProvider(expressionManager, slackConnection).resolve();
        Set<Value> ims = new UsersValueProvider(expressionManager, slackConnection).resolve();

        allChannels.addAll(channels);
        allChannels.addAll(groups);
        allChannels.addAll(ims);

        return allChannels;
    }
}
