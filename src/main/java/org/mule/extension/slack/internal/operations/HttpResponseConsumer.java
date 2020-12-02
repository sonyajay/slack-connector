package org.mule.extension.slack.internal.operations;

import org.mule.extension.slack.api.RequestResponse;
import org.mule.extension.slack.internal.error.SlackError;
import org.mule.extension.slack.internal.error.SlackException;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.function.BiConsumer;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.extension.slack.internal.utils.SlackUtils.getBindingContext;
import static org.mule.runtime.api.metadata.MediaType.APPLICATION_JSON;

public class HttpResponseConsumer<P, A> implements BiConsumer<HttpResponse, Throwable> {

    private final String payloadExpression;
    private ExpressionManager expressionManager;
    private String attributesExpression;
    private SlackError slackError;
    private CompletionCallback<P, A> callback;

    private final static String RESULT_EXPRESSION = "#[output application/java --- {\n" +
            "    \"original\" : write(payload, \"application/json\"),\n" +
            "    \"ok\" : payload.ok,\n" +
            "    \"error\" : payload.error\n" +
            "} as Object {class: \"org.mule.extension.slack.api.RequestResponse\"}]";


    public HttpResponseConsumer(String payloadExpression, SlackError slackError, CompletionCallback<P, A> callback, ExpressionManager expressionManager) {
        this.slackError = slackError;
        this.callback = callback;
        this.payloadExpression = payloadExpression;
        this.expressionManager = expressionManager;
    }

    public HttpResponseConsumer(String payloadExpression, String attributesExpression, SlackError slackError, CompletionCallback<P, A> callback, ExpressionManager expressionManager) {
        this.slackError = slackError;
        this.callback = callback;
        this.attributesExpression = attributesExpression;
        this.payloadExpression = payloadExpression;
        this.expressionManager = expressionManager;
    }

    @Override
    public void accept(HttpResponse httpResponse, Throwable throwable) {
        try {
            if (httpResponse.getStatusCode() == 413) {
                callback.error(new SlackException("Entity too long, your message or attachments are too long.", EXECUTION));
            } else if (httpResponse.getStatusCode() == 429) {
                callback.error(new SlackException("Too many request, try after: " + httpResponse.getHeaderValueIgnoreCase("Retry-After"), EXECUTION));
            } else {
                processHttpResponse(callback, payloadExpression, attributesExpression, getBindingContext(httpResponse.getEntity().getContent()), slackError);
            }
        } catch (Throwable t) {
            callback.error(t);
        }
    }

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
            TypedValue<?> evaluate = expressionManager.evaluate(attributesExpression, getBindingContext(value.getOriginal()));
            MediaType mediaType = evaluate.getDataType().getMediaType();
            Object unwrap = evaluate.getValue();

            if (unwrap != null) {
                resultBuilder.attributes(unwrap);
            } else {
                resultBuilder.attributes(getEmptyMap(mediaType));
            }

            resultBuilder.attributesMediaType(mediaType);
        }

        callback.success(resultBuilder.build());
    }

    private Object getEmptyMap(MediaType mediaType) {
        if (mediaType.toString().toLowerCase().contains("json")) {
            return "{}";
        } else {
            return emptyMap();
        }
    }

    private SlackError getError(String error, SlackError defaultError) {
        try {
            return SlackError.valueOf(error.toUpperCase());
        } catch (Exception e) {
            return defaultError;
        }
    }
}
