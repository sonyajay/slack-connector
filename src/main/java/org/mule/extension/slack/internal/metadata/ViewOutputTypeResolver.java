package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.resolving.OutputStaticTypeResolver;

public class ViewOutputTypeResolver extends OutputStaticTypeResolver {

    @Override
    public MetadataType getStaticMetadata() {
        return getMetadataTypeFromResource("metadata/view-response-schema.json", "View Response");
    }
}