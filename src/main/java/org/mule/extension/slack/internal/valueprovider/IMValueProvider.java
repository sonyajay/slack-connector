package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.IMOperations;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.util.Map;

public class IMValueProvider extends BaseValueProvider {

    public IMValueProvider() {
        super("id", "user");
    }

    public IMValueProvider(ExpressionManager expressionManager, SlackConnection slackConnection) {
        super("id", "user", expressionManager, slackConnection);
    }

    @Override
    PagingProvider<SlackConnection, Map<String, Object>> getPagingProvider() {
        IMOperations imOperations = new IMOperations();
        imOperations.setExpressionManager(expressionManager);
        return imOperations.listIms( 0);
    }
}
