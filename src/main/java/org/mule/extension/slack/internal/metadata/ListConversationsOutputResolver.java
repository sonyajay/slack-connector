package org.mule.extension.slack.internal.metadata;

import static org.mule.metadata.api.model.MetadataFormat.JSON;

import org.mule.extension.slack.internal.ConversationTypes;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class ListConversationsOutputResolver implements OutputTypeResolver<ConversationTypes> {
    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, ConversationTypes types) throws MetadataResolvingException, ConnectionException {
        ObjectTypeBuilder objectTypeBuilder = BaseTypeBuilder.create(JSON).objectType();

        if (types.isPublicChannels()) {
            objectTypeBuilder.addField().key("publicChannels").value().stringType();
        }

        if (types.isPrivateChannels()) {
            objectTypeBuilder.addField().key("isPrivateChannels").value().stringType();
        }

        if (types.isIm()) {
            objectTypeBuilder.addField().key("isIm").value().stringType();
        }

        if (types.isMpim()) {
            objectTypeBuilder.addField().key("isMpim").value().stringType();
        }

        return objectTypeBuilder.build();
    }

    @Override
    public String getCategoryName() {
        return "conversations";
    }
}
