package org.mule.extension.slack.internal.source;

import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.io.InputStream;

public class ResponseBuilder {

    @Optional(defaultValue = "#[output application/json --- payload]")
    @Parameter
    TypedValue<InputStream> response;

    public TypedValue<InputStream> getResponse() {
        return response;
    }
}
