package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.resolving.InputStaticTypeResolver;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;

public class ViewInputTypeResolver extends InputStaticTypeResolver {
    @Override
    public MetadataType getStaticMetadata() {
        return getMetadataTypeFromResource("metadata/views-schema.json", "View");
    }
}
