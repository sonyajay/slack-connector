package org.mule.extension.slack.internal.valueprovider;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static org.mule.extension.slack.internal.utils.SlackUtils.getBindingContext;

import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class ValueProviderCompletionCallback implements CompletionCallback {

    private final Reference<Set<Value>> values;
    private final Reference<ValueResolvingException> exception;
    private final CountDownLatch countDownLatch;
    private final String name;
    private final String id;
    private ExpressionManager expressionManager;

    ValueProviderCompletionCallback(ExpressionManager expressionManager, String name, String id) {
        this.expressionManager = expressionManager;
        this.name = name;
        this.id = id;
        this.values = new Reference<>();
        this.exception = new Reference<>();
        this.countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void success(Result result) {
        Object output = result.getOutput();
        TypedValue<?> evaluate = expressionManager.evaluate("#[output application/java --- payload map { name : $." + name + ", id: $." + id + "}]", getBindingContext(output));
        Set<Value> collect = ((List<Map<String, String>>) evaluate.getValue()).stream().map(val -> ValueBuilder.newValue(val.get("id")).withDisplayName(val.get("name")).build()).collect(toSet());
        values.set(collect);
        countDownLatch.countDown();
    }

    @Override
    public void error(Throwable throwable) {
        exception.set(new ValueResolvingException("An error occurred trying to obtain current Slack Users.", "UNKNOWN", throwable));
        countDownLatch.countDown();
    }

    public Set<Value> getResult() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            return emptySet();
        }

        return values.get();
    }

    public ValueResolvingException getException() {
        return exception.get();
    }
}
