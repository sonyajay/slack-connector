package org.mule.extension.slack.internal.source;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.extension.ExtensionManager;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;
import org.mule.runtime.extension.api.runtime.config.ConfigurationProvider;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.entity.InputStreamHttpEntity;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.mule.runtime.http.api.domain.request.HttpRequestContext;
import org.mule.runtime.http.api.server.HttpServer;
import org.mule.runtime.http.api.server.async.HttpResponseReadyCallback;
import org.mule.runtime.http.api.server.async.ResponseStatusCallback;

import java.io.InputStream;
import java.util.Optional;

import javax.inject.Inject;

abstract class WebhookBasedSource extends Source<InputStream, Void> {

    @ConfigReference(namespace = "HTTP", name = "LISTENER_CONFIG")
    @Parameter
    private String listenerConfig;

    @Inject
    ExtensionManager extensionManager;

    @Parameter
    String path;

    @Override
    public void onStart(SourceCallback<InputStream, Void> sourceCallback) throws MuleException {
        Optional<ConfigurationProvider> configurationProvider = extensionManager.getConfigurationProvider(listenerConfig);
        HttpServer connect = (HttpServer) configurationProvider.get().get(null).getConnectionProvider().get().connect();
        connect.addRequestHandler(path, (requestContext, responseCallback) -> {
            SourceCallbackContext context = sourceCallback.createContext();
            context.addVariable("callback", responseCallback);
            sourceCallback.handle(handleRequest(requestContext), context);

        });
    }

    abstract Result handleRequest(HttpRequestContext requestContext);

    @OnSuccess
    public void onSuccess(SourceCallbackContext callbackContext, @Placement( tab = "Response") @ParameterGroup(showInDsl = true, name = "Slack Response") ResponseBuilder response) {
        onEnd(callbackContext, response.getResponse());
    }

    @OnError
    public void onError(SourceCallbackContext callbackContext, @Placement( tab = "Response") @ParameterGroup(showInDsl = true, name = "Slack Response") ResponseBuilder response) {
        onEnd(callbackContext, response.getResponse());
    }

    private void onEnd(SourceCallbackContext callbackContext, TypedValue<InputStream> response) {
        Optional<HttpResponseReadyCallback> callback = callbackContext.getVariable("callback");
        HttpEntity httpEntity;

        HttpResponse httpResponse;
        if(response != null) {
            if (response.getByteLength().isPresent()) {
                httpEntity = new InputStreamHttpEntity(response.getValue(), response.getByteLength().getAsLong());
            } else {
                httpEntity = new InputStreamHttpEntity(response.getValue());
            }

            httpResponse = HttpResponse
                    .builder()
                    .entity(httpEntity)
                    .addHeader("Content-Type", response.getDataType().getMediaType().toRfcString()).build();
        } else {
            httpResponse = HttpResponse
                    .builder()
                    .build();
        }

        callback.ifPresent(responseCallback -> responseCallback.responseReady(httpResponse, new ResponseStatusCallback() {
            @Override
            public void responseSendFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void responseSendSuccessfully() {
                System.out.println("Success");
            }
        }));
    }

    @Override
    public void onStop() {

    }
}
