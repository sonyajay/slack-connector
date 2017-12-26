package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.operations.UsersOperations;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.util.function.Consumer;

public class UsersValueProvider extends BaseValueProvider {

    public UsersValueProvider() {
        super("id", "name");
    }

    @Override
    Consumer<CompletionCallback> execute() {
        return completionCallback -> {
            UsersOperations usersOperations = new UsersOperations();
            usersOperations.setExpressionManager(expressionManager);
            usersOperations.listUsers(slackConnection, null, false, 0, false, completionCallback);
        };
    }
}
