package org.mule.extension.slack.internal.connection;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.Placement;

public abstract class SlackBaseConnectionProvider implements CachedConnectionProvider<SlackConnection> {

    @Parameter
    @Placement(tab = "Advanced")
    @Optional(defaultValue = "5000")
    int connectionTimeout;

    @ParameterGroup(name = "Proxy Config")
    SlackProxyConfig proxyConfig;


    @Override
    public void disconnect(SlackConnection connection) {
        connection.disconnect();
    }

    @Override
    public ConnectionValidationResult validate(SlackConnection connection) {
        return ConnectionValidationResult.success();
    }

}
