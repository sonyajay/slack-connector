package org.mule.extension.slack.internal.operations;

import static java.lang.String.format;
import static org.mule.extension.slack.internal.utils.SlackUtils.getBindingContext;
import static org.mule.runtime.api.metadata.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.api.RequestResponse;
import org.mule.extension.slack.internal.error.SlackError;
import org.mule.extension.slack.internal.error.SlackException;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.Ignore;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import javax.inject.Inject;

/**
 * test-extension-1
 *
 * @author Esteban Wasinger (http://github.com/estebanwasinger)
 */
public abstract class SlackOperations {

    @Inject
    ExpressionManager expressionManager;

    @Ignore
    public void setExpressionManager(ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }

    @Ignore
    public HttpResponseConsumer<HttpResponse, Throwable> createConsumer(String payloadExpression, SlackError slackError, CompletionCallback callback) {
        return new HttpResponseConsumer<>(payloadExpression, slackError, callback, expressionManager);
    }

    @Ignore
    public HttpResponseConsumer<HttpResponse, Throwable> createConsumer(String payloadExpression, String attributesExpression, SlackError slackError, CompletionCallback callback) {
        return new HttpResponseConsumer<>(payloadExpression, attributesExpression, slackError, callback, expressionManager);
    }

}
