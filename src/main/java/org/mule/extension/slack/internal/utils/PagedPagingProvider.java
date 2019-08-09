package org.mule.extension.slack.internal.utils;

import static java.util.Collections.emptyList;
import static org.mule.extension.slack.internal.error.SlackError.CHANNEL_LISTING;
import static org.mule.runtime.core.api.util.StringUtils.isEmpty;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.HttpResponseConsumer;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;

import org.mulesoft.common.ext.Diff.Str;

public class PagedPagingProvider implements PagingProvider<SlackConnection, Map<String, Object>> {

    private String nextCursor = null;
    private boolean shouldClose = false;
    private BiFunction<SlackConnection, String, CompletableFuture<HttpResponse>> futureFunction;
    private String payloadExpression;
    private ExpressionManager expressionManager;

    public PagedPagingProvider(BiFunction<SlackConnection, String, CompletableFuture<HttpResponse>> futureFunction, String payloadExpression, ExpressionManager expressionManager) {
        this.futureFunction = futureFunction;
        this.payloadExpression = payloadExpression;
        this.expressionManager = expressionManager;
    }

    @Override
    public List<Map<String, Object>> getPage(SlackConnection connection) {

        if(shouldClose) {
            return emptyList();
        }

        Reference<Result<List<Map<String, Object>>, Map<String, Object>>> resultReference = new Reference<>();
        Reference<Throwable> errorReference = new Reference<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        HttpResponseConsumer<List<Map<String, Object>>, Map<String, Object>> consumer = new HttpResponseConsumer<>(payloadExpression, "#[output application/java --- payload.paging]", CHANNEL_LISTING, new CompletionCallback<List<Map<String, Object>>, Map<String, Object>>() {
            @Override
            public void success(Result<List<Map<String, Object>>, Map<String, Object>> result) {
                resultReference.set(result);
                countDownLatch.countDown();
            }

            @Override
            public void error(Throwable e) {
                errorReference.set(e);
                countDownLatch.countDown();
            }
        }, expressionManager);

        futureFunction.apply(connection, nextCursor)
                .whenComplete(consumer);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(errorReference.get() != null) {
            throw new RuntimeException(errorReference.get());
        }

        Integer page = ((Integer) resultReference.get().getAttributes().get().get("page")) + 1;
        nextCursor = page.toString();

        if(isEmpty(nextCursor)) {
            shouldClose = true;
        }

        return resultReference.get().getOutput();
    }

    @Override
    public java.util.Optional<Integer> getTotalResults(SlackConnection connection) {
        return java.util.Optional.empty();
    }

    @Override
    public void close(SlackConnection connection) throws MuleException {

    }
}
