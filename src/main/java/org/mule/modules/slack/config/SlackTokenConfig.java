/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.config;

import static org.mule.module.http.internal.request.DefaultHttpRequesterConfig.OBJECT_HTTP_CLIENT_FACTORY;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.MuleRuntimeException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.context.MuleContextAware;
import org.mule.module.http.internal.request.HttpClient;
import org.mule.module.http.internal.request.HttpClientConfiguration;
import org.mule.module.http.internal.request.HttpClientFactory;
import org.mule.module.http.internal.request.grizzly.GrizzlyHttpClient;
import org.mule.modules.slack.client.JerseySlackRequestBuilder;
import org.mule.modules.slack.client.MuleHttpClientSlackRequestBuilder;
import org.mule.modules.slack.client.SlackClient;

/**
 * Connection Management Strategy
 *
 * @author Esteban Wasinger.
 */
@ConnectionManagement(configElementName = "token-config", friendlyName = "Token Configuration")
public class SlackTokenConfig extends SlackConfig implements MuleContextAware {

    private SlackClient slackClient;
    private String accessToken;
    private MuleContext muleContext;

    /**
     * @throws ConnectionException
     */
    @TestConnectivity
    @Connect
    public void connect(@ConnectionKey String accessToken) throws ConnectionException {
        slackClient = createSlackClient(accessToken, muleContext);

        if (!slackClient.auth.isConnected()) {
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "Invalid Token", "Invalid Token");
        }
    }

    @Disconnect
    public void disconnect() {
        slackClient = null;
        try {
            if(httpClient != null){
                httpClient.stop();
            }
        } catch (Exception e){
            throw new MuleRuntimeException(e);
        }
    }

    @ValidateConnection
    public boolean isValid() {
        return slackClient != null && slackClient.auth.isConnected();
    }

    @ConnectionIdentifier
    public String getConnectionIdentifier() {
        return accessToken;
    }

    public SlackClient getSlackClient() {
        return slackClient;
    }

    public String getToken() {
        return accessToken;
    }

    public void setToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        slackClient = new SlackClient(accessToken);
        this.accessToken = accessToken;
    }

    public Boolean isAuthorized() {
        return accessToken != null;
    }

    @Override
    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }
}