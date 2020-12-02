package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.InputStaticTypeResolver;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;

public class BlocksTypeResolver extends InputStaticTypeResolver {

    @Override
    public MetadataType getStaticMetadata() {
        return getMetadataTypeFromResource("metadata/attachments-schema.json", "Attachments");
    }
}
