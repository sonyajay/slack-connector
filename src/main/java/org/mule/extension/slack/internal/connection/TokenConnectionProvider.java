package org.mule.extension.slack.internal.connection;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.http.api.HttpService;

import javax.inject.Inject;

@DisplayName("1 - Token Connection")
@Alias("token-connection")
public class TokenConnectionProvider extends SlackBaseConnectionProvider {

    @Inject
    HttpService httpService;

    @Parameter
    private String token;

    @Override
    public SlackConnection connect() throws ConnectionException {
        return new SlackConnection(token, httpService, proxyConfig.getProxyConfig().orElse(null));
    }
}
