package org.keycloak.adapters.undertow;

import org.ops4j.pax.web.service.spi.auth.AuthenticatorService;

public class UndertowAuthService implements AuthenticatorService {

    @Override
    public <T> T getAuthenticatorService(String method, Class<T> iface) {
        if ("KEYCLOAK".equals(method) && iface == io.undertow.servlet.ServletExtension.class) {
            return iface.cast(new KeycloakServletExtension());
        }
        return null;
    }

}
