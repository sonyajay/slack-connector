package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;

public class PostMessageAttributesResolver implements AttributesTypeResolver {

    @Override
    public MetadataType getAttributesType(MetadataContext metadataContext, Object o) throws MetadataResolvingException, ConnectionException {
        return getMetadataTypeFromResource("metadata/post-message-attributes-schema.json", "Post Message Result Attributes");
    }

    @Override
    public String getCategoryName() {
        return "channels";
    }
}
