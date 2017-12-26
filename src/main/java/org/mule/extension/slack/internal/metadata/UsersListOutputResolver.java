package org.mule.extension.slack.internal.metadata;

public class UsersListOutputResolver extends BaseOutputTypeResolver {

    @Override
    public String getResource() {
        return "metadata/users-list-schema.json";
    }

    @Override
    public String getAlias() {
        return "User List";
    }

    @Override
    public String getCategoryName() {
        return "users-list";
    }
}
