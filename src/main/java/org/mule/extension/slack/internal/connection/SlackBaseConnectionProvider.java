package org.mule.extension.slack.internal.connection;

import static org.mule.runtime.api.connection.ConnectionValidationResult.failure;
import static org.mule.runtime.api.connection.ConnectionValidationResult.success;

import org.mule.extension.slack.api.RequestResponse;
import org.mule.extension.slack.internal.error.SlackError;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.scheduler.Scheduler;
import org.mule.runtime.api.scheduler.SchedulerService;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

public abstract class SlackBaseConnectionProvider implements CachedConnectionProvider<SlackConnection> {

    private static final DataType JSON_TYPE = DataType.builder().type(InputStream.class).mediaType(MediaType.APPLICATION_JSON).build();
    private static final String JAVA_PAYLOAD = "#[output application/java --- { ok : payload.ok, error : payload.error } as Object {class : 'org.mule.extension.slack.api.RequestResponse'}]";

    @Parameter
    @Placement(tab = "Advanced")
    @Optional(defaultValue = "5000")
    int connectionTimeout;

    @ParameterGroup(name = "Proxy Config")
    SlackProxyConfig proxyConfig;

    @Inject
    ExpressionManager expressionManager;

    @Inject
    SchedulerService schedulerService;


    @Override
    public void disconnect(SlackConnection connection) {
        connection.disconnect();
    }

    @Override
    public ConnectionValidationResult validate(SlackConnection connection) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Reference<ConnectionValidationResult> result = new Reference<>();
        connection.auth.test().whenCompleteAsync((httpResponse, throwable) -> {
            if(throwable != null){
                result.set(failure("Unable to connect to Slack." + throwable.getMessage(), new ConnectionException(throwable)));
                countDownLatch.countDown();
                return;
            }

            RequestResponse requestResponse = null;
            try {
                requestResponse = getRequestResponse(httpResponse);
            } catch (InterruptedException e) {
                result.set(failure(e.getMessage(), e));
                countDownLatch.countDown();
            }

            if(requestResponse.isOk()){
                result.set(success());
            } else {
                String errorType = requestResponse.getError();
                SlackError slackError;
                try {
                    slackError = SlackError.valueOf(errorType);
                } catch (Exception e){
                    slackError = SlackError.INVALID_AUTH;
                }

                result.set(failure("Unable to connect to Slack. " + errorType, new ConnectionException(new ModuleException("Unable to connect to Slack.", slackError))));
            }
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            return failure(e.getMessage(), e);
        }
        return result.get();
    }

    private RequestResponse getRequestResponse(HttpResponse httpResponse) throws InterruptedException {
        Reference<RequestResponse> requestResponseReference = new Reference<>();
        Scheduler scheduler = schedulerService.ioScheduler();
        CountDownLatch latch = new CountDownLatch(1);
        scheduler.submit(() -> {
            try {
                requestResponseReference.set((RequestResponse) expressionManager.evaluate(JAVA_PAYLOAD, BindingContext
                        .builder()
                        .addBinding("payload", new TypedValue<>(httpResponse.getEntity().getContent(), JSON_TYPE))
                        .build())
                        .getValue());
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } finally {
            scheduler.stop();
        }

        return requestResponseReference.get();
    }

}
