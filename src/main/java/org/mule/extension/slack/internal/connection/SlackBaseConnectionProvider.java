package org.mule.extension.slack.internal.connection;

import static org.mule.runtime.api.connection.ConnectionValidationResult.failure;
import static org.mule.runtime.api.connection.ConnectionValidationResult.success;

import org.mule.extension.slack.internal.error.SlackError;
import org.mule.extension.slack.internal.operations.SlackOperations;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract class SlackBaseConnectionProvider implements CachedConnectionProvider<SlackConnection> {

    @Parameter
    @Placement(tab = "Advanced")
    @Optional(defaultValue = "5000")
    int connectionTimeout;

    @ParameterGroup(name = "Proxy Config")
    SlackProxyConfig proxyConfig;


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

            String response = IOUtils.toString(httpResponse.getEntity().getContent());
            Map<String, Object> javaResponse = new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {
            }.getType());

            Boolean isOk = (Boolean) javaResponse.get("ok");
            if(isOk){
                result.set(success());
            } else {
                String errorType = (String) javaResponse.get("error");
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

}
