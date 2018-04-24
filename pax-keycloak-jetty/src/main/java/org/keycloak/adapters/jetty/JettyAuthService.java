package org.keycloak.adapters.jetty;

import org.ops4j.pax.web.service.AuthenticatorService;

public class JettyAuthService implements AuthenticatorService {

    @Override
    public <T> T getAuthenticatorService(String method, Class<T> iface) {
        if ("KEYCLOAK".equals(method) && iface == org.eclipse.jetty.security.Authenticator.class) {
            return iface.cast(new KeycloakJettyAuthenticator());
        }
        return null;
    }

}
