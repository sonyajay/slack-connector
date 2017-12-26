package org.mule.extension.slack.internal.metadata;

public class ChannelInfoOutputResolver extends BaseOutputTypeResolver {

    @Override
    public String getResource() {
        return "metadata/channel-info-schema.json";
    }

    @Override
    public String getAlias() {
        return "Channel Info";
    }

    @Override
    public String getCategoryName() {
        return "channel-info";
    }
}
