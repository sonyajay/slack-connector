package org.mule.extension.slack.internal.source;

import static org.mule.runtime.api.metadata.MediaType.parse;

import org.mule.runtime.extension.api.annotation.metadata.MetadataScope;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.source.EmitsResponse;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.http.api.domain.request.HttpRequestContext;

import java.io.InputStream;

@MediaType(value = "*/*", strict = false)
@MetadataScope(outputResolver = SlashCommandTypeResolver.class)
@DisplayName("On Slash Command")
@EmitsResponse()
public class OnSlashCommand extends WebhookBasedSource {

    @Override
    Result handleRequest(HttpRequestContext requestContext) {
        InputStream content = requestContext.getRequest().getEntity().getContent();
        String contentType = requestContext.getRequest().getHeaderValueIgnoreCase("content-type");

        if (contentType == null) {
            contentType = "*/*";
        }

        return Result.<InputStream, Void>builder()
                .output(content)
                .mediaType(parse(contentType))
                .build();
    }
}
