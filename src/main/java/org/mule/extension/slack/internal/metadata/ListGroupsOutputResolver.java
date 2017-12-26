package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class ListGroupsOutputResolver implements OutputTypeResolver {

    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, Object o) throws MetadataResolvingException, ConnectionException {
        return getMetadataTypeFromResource("metadata/group-list-schema.json", "Channel List");
    }

    @Override
    public String getCategoryName() {
        return "list-group";
    }
}
