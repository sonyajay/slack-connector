package org.mule.extension.slack.internal.source;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class ListenerOutputResolver implements OutputTypeResolver {
    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, Object o) {

        ObjectTypeBuilder objectTypeBuilder = BaseTypeBuilder.create(JAVA).objectType();
        objectTypeBuilder.addField().key("type").value().stringType();
        objectTypeBuilder.addField().key("channel").value().stringType();
        objectTypeBuilder.addField().key("user").value().stringType();
        objectTypeBuilder.addField().key("text").value().stringType();
        objectTypeBuilder.addField().key("ts").value().stringType();
        objectTypeBuilder.addField().key("source_team").value().stringType();
        objectTypeBuilder.addField().key("team").value().stringType();
        objectTypeBuilder.id("Incoming Slack Message");
        return objectTypeBuilder.build();
    }

    @Override
    public String getCategoryName() {
        return "listener";
    }
}
