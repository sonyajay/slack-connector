package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.UsersOperations;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    BaseValueProvider(String id, String name, ExpressionManager expressionManager, SlackConnection slackConnection) {
        this.id = id;
        this.name = name;
        this.expressionManager = expressionManager;
        this.slackConnection = slackConnection;
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

    private Consumer<CompletionCallback> execute() {
        return completionCallback -> {
            try {
                PagingProvider<SlackConnection, Map<String, Object>> slackConnectionMapPagingProvider = getPagingProvider();

                List<Map<String, Object>> page = slackConnectionMapPagingProvider.getPage(slackConnection);
                List<Map<String, Object>> accumulator = new ArrayList<>();
                while (!page.isEmpty()) {
                    accumulator.addAll(page);
                    page = slackConnectionMapPagingProvider.getPage(slackConnection);
                }

                completionCallback.success(Result.builder().output(accumulator).build());
            } catch (Throwable t) {
                completionCallback.error(t);
            }
        };
    }

    abstract PagingProvider<SlackConnection, Map<String, Object>> getPagingProvider();
}
