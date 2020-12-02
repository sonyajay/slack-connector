package org.mule.extension.slack.internal.utils;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.json.api.JsonTypeLoader;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.util.IOUtils;

import java.io.InputStream;

public class SlackUtils {
    static final DataType APPLICATION_JSON = DataType.builder().type(InputStream.class).mediaType(MediaType.APPLICATION_JSON).build();


    public static BindingContext getBindingContext(Object response) {
        return BindingContext.builder().addBinding("payload", new TypedValue<>(response, APPLICATION_JSON)).build();
    }

    public static BindingContext getJavaBindingContext(Object response) {
        return BindingContext.builder().addBinding("payload", new TypedValue<>(response, DataType.OBJECT)).build();
    }

    public static MetadataType getMetadataTypeFromResource(String resource, String typeAlias) {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        return new JsonTypeLoader(IOUtils.toString(resourceAsStream)).load(typeAlias, typeAlias).get();
    }
}
