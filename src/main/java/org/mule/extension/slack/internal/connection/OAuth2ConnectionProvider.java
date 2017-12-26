package org.mule.extension.slack.internal.connection;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.AuthorizationCode;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeState;
import org.mule.runtime.http.api.HttpService;

import javax.inject.Inject;

@AuthorizationCode(accessTokenUrl = "https://slack.com/api/oauth.access",
        authorizationUrl = "https://slack.com/oauth/authorize",
        defaultScopes = "chat:write:user")
@Alias("oauth-connection")
@DisplayName("2 - OAuth 2 Connection")
public class OAuth2ConnectionProvider extends SlackBaseConnectionProvider {

    AuthorizationCodeState state;

    @Inject
    private HttpService httpService;

    @Override
    public SlackConnection connect() throws ConnectionException {
        return new SlackConnection(state.getAccessToken(), httpService, proxyConfig.getProxyConfig().orElse(null));
    }
}
