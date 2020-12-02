package org.mule.extension.slack.internal.source;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.impl.DefaultStringType;
import org.mule.runtime.api.metadata.resolving.OutputStaticTypeResolver;

public class SlashCommandTypeResolver extends OutputStaticTypeResolver {
    @Override
    public MetadataType getStaticMetadata() {
        MetadataFormat format = new MetadataFormat("URL Encoded", "URL Encoded", "application/x-www-form-urlencoded");
        DefaultStringType stringType = BaseTypeBuilder.create(format).stringType().build();
        ObjectTypeBuilder objectTypeBuilder = BaseTypeBuilder.create(format)
                .objectType();
        objectTypeBuilder.description("Slash Command Info");
        objectTypeBuilder.label("Slash Command Info");
        objectTypeBuilder.addField().required().key("token").value(stringType);
        objectTypeBuilder.addField().required().key("team_id").value(stringType);
        objectTypeBuilder.addField().required().key("team_domain").value(stringType);
        objectTypeBuilder.addField().required().key("channel_id").value(stringType);
        objectTypeBuilder.addField().required().key("channel_name").value(stringType);
        objectTypeBuilder.addField().required().key("user_id").value(stringType);
        objectTypeBuilder.addField().required().key("user_name").value(stringType);
        objectTypeBuilder.addField().required().key("command").value(stringType);
        objectTypeBuilder.addField().required().key("text").value(stringType);
        objectTypeBuilder.addField().required().key("api_app_id").value(stringType);
        objectTypeBuilder.addField().required().key("response_url").value(stringType);
        objectTypeBuilder.addField().required().key("trigger_id").value(stringType);

        return objectTypeBuilder.build();
    }
}
