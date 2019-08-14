package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.core.api.util.func.CheckedSupplier;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

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

        addAllQuitely(allChannels, () -> new ChannelsValueProvider(expressionManager, slackConnection).resolve());
        addAllQuitely(allChannels, () -> new GroupsValueProvider(expressionManager, slackConnection).resolve());
        addAllQuitely(allChannels, () -> new UsersValueProvider(expressionManager, slackConnection).resolve());

        return allChannels;
    }

    public void addAllQuitely(Set<Value> allValues, CheckedSupplier<Set<Value>> valueSupplier) {
        try {
            allValues.addAll(valueSupplier.get());
        } catch (Throwable t) {
            //
        }
    }
}
