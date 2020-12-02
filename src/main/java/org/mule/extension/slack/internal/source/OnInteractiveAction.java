package org.mule.extension.slack.internal.source;

import static org.mule.runtime.api.metadata.MediaType.parse;

import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.DataTypeBuilder;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.core.api.extension.ExtensionManager;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.annotation.metadata.MetadataScope;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;
import org.mule.runtime.extension.api.annotation.source.EmitsResponse;
import org.mule.runtime.extension.api.runtime.config.ConfigurationProvider;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.mule.runtime.http.api.domain.request.HttpRequestContext;
import org.mule.runtime.http.api.server.HttpServer;
import org.mule.runtime.http.api.server.async.HttpResponseReadyCallback;
import org.mule.runtime.http.api.server.async.ResponseStatusCallback;

import java.io.InputStream;
import java.util.Optional;

import javax.inject.Inject;

@MediaType(value = "*/*", strict = false)
@OutputJsonType(schema = "metadata/on-new-interactive-action-schema.json")
@DisplayName("On Interactive Action")
@EmitsResponse()
public class OnInteractiveAction extends WebhookBasedSource {

    @Inject
    ExpressionManager expressionManager;

    @Override
    Result handleRequest(HttpRequestContext requestContext) {
        InputStream content = requestContext.getRequest().getEntity().getContent();
        String contentType = requestContext.getRequest().getHeaderValueIgnoreCase("content-type");

        if(contentType == null) {
            contentType = "*/*";
        }

        TypedValue payload = expressionManager.evaluate("#[output application/json --- read(payload.payload, 'application/json')]", BindingContext.builder().addBinding("payload", new TypedValue<>(content, DataType.builder().type(InputStream.class).mediaType(contentType).build())).build());

        return Result.builder().output(payload.getValue()).mediaType(parse("application/json")).build();
    }
}
