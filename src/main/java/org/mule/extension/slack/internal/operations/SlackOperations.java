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

import java.util.function.BiConsumer;

/**
 * test-extension-1
 *
 * @author Esteban Wasinger (http://github.com/estebanwasinger)
 */
public abstract class SlackOperations {

    private final static String RESULT_EXPRESSION = "#[output application/java --- {\n" +
            "    \"original\" : write(payload, \"application/json\"),\n" +
            "    \"ok\" : payload.ok,\n" +
            "    \"error\" : payload.error\n" +
            "} as Object {class: \"org.mule.extension.slack.api.RequestResponse\"}]";
    @Inject
    ExpressionManager expressionManager;

    private void processHttpResponse(CompletionCallback callback, String payloadExpression, String attributesExpression, BindingContext bindingContext, SlackError defaultError) {
        TypedValue<?> resultWrapper = expressionManager.evaluate(RESULT_EXPRESSION, bindingContext);

        RequestResponse value = (RequestResponse) resultWrapper.getValue();

        if (!value.isOk()) {
            throw new SlackException(format("An error occurred calling the Slack API. \n Error Code: %s. \n Original Error Message: \n %s \n", value.getError(), value.getOriginal()), getError(value.getError(), defaultError));
        }

        Object payload = expressionManager.evaluate(payloadExpression, getBindingContext(value.getOriginal())).getValue();

        Result.Builder resultBuilder = Result.builder()
                .output(payload)
                .mediaType(APPLICATION_JSON);

        if (attributesExpression != null) {
            Object unwrap = expressionManager.evaluate(attributesExpression, getBindingContext(value.getOriginal())).getValue();
            if (unwrap != null) {
                resultBuilder.attributes(unwrap);
            } else {
                resultBuilder.attributes("{}");
            }
            resultBuilder.mediaType(APPLICATION_JSON);
        }

        callback.success(resultBuilder.build());
    }

    private SlackError getError(String error, SlackError defaultError) {
        try {
            return SlackError.valueOf(error.toUpperCase());
        } catch (Exception e) {
            return defaultError;
        }
    }

    @Ignore
    public void setExpressionManager(ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }

    public class HttpResponseConsumer<P, A> implements BiConsumer<HttpResponse, Throwable> {

        private final String payloadExpression;
        private String attributesExpression;
        private SlackError slackError;
        private CompletionCallback<P, A> callback;

        HttpResponseConsumer(String payloadExpression, SlackError slackError, CompletionCallback<P, A> callback) {
            this.slackError = slackError;
            this.callback = callback;
            this.payloadExpression = payloadExpression;
        }

        HttpResponseConsumer(String payloadExpression, String attributesExpression, SlackError slackError, CompletionCallback<P, A> callback) {
            this.slackError = slackError;
            this.callback = callback;
            this.attributesExpression = attributesExpression;
            this.payloadExpression = payloadExpression;
        }

        @Override
        public void accept(HttpResponse httpResponse, Throwable throwable) {
            try {
                processHttpResponse(callback, payloadExpression, attributesExpression, getBindingContext(httpResponse.getEntity().getContent()), slackError);
            } catch (Throwable t) {
                callback.error(t);
            }
        }
    }
}
