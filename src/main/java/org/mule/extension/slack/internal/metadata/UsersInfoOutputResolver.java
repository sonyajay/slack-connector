package org.mule.extension.slack.internal.metadata;

public class UsersInfoOutputResolver extends BaseOutputTypeResolver {

    @Override
    public String getResource() {
        return "metadata/user-info-schema.json";
    }

    @Override
    public String getAlias() {
        return "User Info";
    }

    @Override
    public String getCategoryName() {
        return "users-info";
    }
}