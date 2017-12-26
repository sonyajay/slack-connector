package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.operations.GroupOperations;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.util.function.Consumer;

public class GroupsValueProvider extends BaseValueProvider {

    public GroupsValueProvider() {
        super("id", "name");
    }

    @Override
    Consumer<CompletionCallback> execute() {
        return completionCallback -> {
            GroupOperations groupOperations = new GroupOperations();
            groupOperations.setExpressionManager(expressionManager);
            groupOperations.listGroups(slackConnection, true, true, completionCallback);
        };
    }
}
