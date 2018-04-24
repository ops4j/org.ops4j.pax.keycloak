package org.keycloak.adapters.tomcat;

import org.ops4j.pax.web.service.AuthenticatorService;

public class TomcatAuthService implements AuthenticatorService {

    @Override
    public <T> T getAuthenticatorService(String method, Class<T> iface) {
        if ("KEYCLOAK".equals(method) && iface == org.apache.catalina.Valve.class) {
            return iface.cast(new KeycloakAuthenticatorValve());
        }
        return null;
    }

}
