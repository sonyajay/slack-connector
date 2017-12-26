package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class OpenIMOutputResolver implements OutputTypeResolver<Boolean>, AttributesTypeResolver {

    private static final String OPEN_IM = "open-im";

    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, Boolean returnIM) {
        return returnIM
                ? getMetadataTypeFromResource("metadata/im-open-show-im.json", "Open IM")
                : getMetadataTypeFromResource("metadata/im-open-schema.json", "Open IM");
    }

    @Override
    public String getCategoryName() {
        return OPEN_IM;
    }

    @Override
    public String getResolverName() {
        return OPEN_IM;
    }

    @Override
    public MetadataType getAttributesType(MetadataContext metadataContext, Object o) throws MetadataResolvingException, ConnectionException {
        return getMetadataTypeFromResource("metadata/im-open-attributes-schema.json", "Open IM Attributes");
    }
}
