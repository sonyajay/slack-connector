package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.UsersOperations;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UsersValueProvider extends BaseValueProvider {

    public UsersValueProvider(ExpressionManager expressionManager, SlackConnection slackConnection) {
        super("id", "name", expressionManager, slackConnection);
    }

    public UsersValueProvider() {
        super("id", "name");
    }

    @Override
    PagingProvider<SlackConnection, Map<String, Object>> getPagingProvider() {
        UsersOperations usersOperations = new UsersOperations();
        usersOperations.setExpressionManager(expressionManager);
        return usersOperations.listUsers(false, 0, false, null);
    }
}
