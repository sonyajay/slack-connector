/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.config;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.modules.slack.client.SlackClient;

/**
 * Connection Management Strategy
 *
 * @author Esteban Wasinger.
 */
@ConnectionManagement(configElementName = "token-config", friendlyName = "Token Configuration")
public class SlackTokenConfig implements SlackConfig {

    private SlackClient slackClient;
    private String accessToken;

    /**
     * @throws ConnectionException
     */
    @TestConnectivity
    @Connect
    public void connect(@ConnectionKey String accessToken) throws ConnectionException {
        slackClient = new SlackClient(accessToken);
        if (!slackClient.auth.isConnected()) {
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "Invalid Token", "Invalid Token");
        }
    }

    @Disconnect
    public void disconnect() {
        slackClient = null;
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
}