package org.mule.extension.slack.internal.connection;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.http.api.client.proxy.BaseProxyConfigBuilder;
import org.mule.runtime.http.api.client.proxy.ProxyConfig;

public class SlackProxyConfig {

    private static final String PROXY_CONFIG = "Proxy Config";

    @Parameter
    @Placement(tab = PROXY_CONFIG)
    @Optional
    private String host;

    @Parameter
    @Placement(tab = PROXY_CONFIG)
    @Optional
    private Integer port;

    @Parameter
    @Placement(tab = PROXY_CONFIG)
    @Optional
    private String username;

    @Parameter
    @Placement(tab = PROXY_CONFIG)
    @Password
    @Optional
    private String password;

    @Parameter
    @Placement(tab = PROXY_CONFIG)
    @Optional
    @DisplayName("NTLM Domain")
    private String ntlmDomain;

    java.util.Optional<ProxyConfig> getProxyConfig() {
        BaseProxyConfigBuilder builder;

        if (ntlmDomain != null) {
            builder = ProxyConfig.NtlmProxyConfig.builder().ntlmDomain(ntlmDomain);
        } else {
            builder = ProxyConfig.builder();
        }

        if (host != null && port != null) {
            builder.host(host);
            builder.port(port);
            builder.username(username);
            builder.password(password);
            return of(builder.build());
        } else {
            return empty();
        }
    }
}
