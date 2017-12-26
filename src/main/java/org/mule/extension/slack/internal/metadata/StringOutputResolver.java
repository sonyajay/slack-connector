package org.mule.extension.slack.internal.metadata;

import static org.mule.metadata.json.api.JsonTypeLoader.JSON;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.impl.DefaultStringType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class StringOutputResolver implements OutputTypeResolver {

    private static final DefaultStringType STRING_TYPE = BaseTypeBuilder.create(JSON).stringType().build();

    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, Object o) {
        return STRING_TYPE;
    }

    @Override
    public String getCategoryName() {
        return "json-string-category";
    }
}
