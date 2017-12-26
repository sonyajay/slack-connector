package org.mule.extension.slack.internal.metadata;

import static org.mule.extension.slack.internal.utils.SlackUtils.getMetadataTypeFromResource;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public abstract class BaseOutputTypeResolver implements OutputTypeResolver {

    public abstract String getResource();

    public abstract String getAlias();

    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, Object o) throws MetadataResolvingException, ConnectionException {
        return getMetadataTypeFromResource(getResource(), getAlias());
    }
}
