package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.ChannelOperations;
import org.mule.extension.slack.internal.operations.GroupOperations;
import org.mule.extension.slack.internal.operations.UsersOperations;
import org.mule.extension.slack.internal.utils.PagingProviderIterator;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class GroupsValueProvider extends BaseValueProvider {

    public GroupsValueProvider() {
        super("id", "name");
    }

    public GroupsValueProvider(ExpressionManager expressionManager, SlackConnection slackConnection) {
        super("id", "name", expressionManager, slackConnection);
    }

    @Override
    PagingProvider<SlackConnection, Map<String, Object>> getPagingProvider() {
        GroupOperations groupOperations = new GroupOperations();
        groupOperations.setExpressionManager(expressionManager);
        return groupOperations.listGroups(true, true, 0);
    }
}
