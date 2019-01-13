package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class ListChannelsOutputResolver implements OutputTypeResolver {

    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, Object o) {
        return getMetadataTypeFromResource("metadata/channel-schema.json", "Channel");
    }

    @Override
    public String getCategoryName() {
        return "channel-list";
    }
}
