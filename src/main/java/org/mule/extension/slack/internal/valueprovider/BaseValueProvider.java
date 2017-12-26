package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import javax.inject.Inject;

import java.util.Set;
import java.util.function.Consumer;

public abstract class BaseValueProvider implements ValueProvider {

    private final String id;
    private final String name;

    @Inject
    ExpressionManager expressionManager;

    @Connection
    SlackConnection slackConnection;

    BaseValueProvider(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Set<Value> resolve() throws ValueResolvingException {
        ValueProviderCompletionCallback callback = new ValueProviderCompletionCallback(expressionManager, name, id);
        execute().accept(callback);

        if (callback.getException() != null) {
            throw callback.getException();
        }

        return callback.getResult();
    }

    abstract Consumer<CompletionCallback> execute();
}
