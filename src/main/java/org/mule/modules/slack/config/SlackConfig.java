/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.config;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Default;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.Startable;
import org.mule.module.http.internal.request.HttpClient;
import org.mule.modules.slack.client.JerseySlackRequestBuilder;
import org.mule.modules.slack.client.MuleHttpClientSlackRequestBuilder;
import org.mule.modules.slack.client.SlackClient;

import com.github.estebanwasinger.http.bridge.api.DefaultHttpClientBridgeFactory;

public abstract class SlackConfig {

    HttpClient httpClient;

    public abstract SlackClient getSlackClient();

    abstract String getToken();

    abstract Boolean isAuthorized();

    @Configurable
    @Default(value = "MULE")
    @Placement(tab = "Advanced", group = "HTTP Client Configuration")
    @FriendlyName("HTTP Client Type")
    private HttpClientType httpClientType;

    SlackClient createSlackClient(String accessToken, MuleContext muleContext) throws ConnectionException {
        switch (httpClientType) {
            case MULE: {
                createMuleHttpClient(muleContext);
                return new SlackClient(accessToken, MuleHttpClientSlackRequestBuilder.getFactory(httpClient));
            }
            case JERSEY:
                return new SlackClient(accessToken, JerseySlackRequestBuilder.getFactory());
            default:
                throw new RuntimeException();
        }
    }

    private void createMuleHttpClient(MuleContext muleContext) throws ConnectionException {
        httpClient = new DefaultHttpClientBridgeFactory(muleContext)
                .createClient(100, true, 10000, "slack-connector", "slack");
        try {
            startHttpClient();
        } catch (MuleException e) {
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, "", "", e);
        }
    }

    private void startHttpClient() throws MuleException {
        if(httpClient instanceof Startable){
            ((Startable) httpClient).start();
        } else if (httpClient instanceof Initialisable){
            ((Initialisable) httpClient).initialise();
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClientType getHttpClientType() {
        return httpClientType;
    }

    public void setHttpClientType(HttpClientType httpClientType) {
        this.httpClientType = httpClientType;
    }
}
