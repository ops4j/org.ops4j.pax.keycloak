/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.keycloak.hawtio;

import org.apache.felix.utils.properties.Properties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Activator implements BundleActivator {

    private static final Logger LOGGER = Logger.getLogger(Activator.class.getName());

    @Override
    public void start(BundleContext context) throws Exception {
        String etc = System.getProperty("karaf.etc");

        // system-property -p hawtio.roles admin,user
        // system-property -p hawtio.keycloakEnabled true
        // system-property -p hawtio.realm keycloak
        // system-property -p hawtio.keycloakClientConfig file://\$\{karaf.etc\}/keycloak-hawtio-client.json
        // system-property -p hawtio.rolePrincipalClasses org.keycloak.adapters.jaas.RolePrincipal,org.apache.karaf.jaas.boot.principal.RolePrincipal
        try {
            Properties props = new Properties(new File(etc, "system.properties"));

            boolean modified = false;
            modified |= setIfNot(props, "hawtio.keycloakEnabled", "true", null, "\n# Hawtio / Keycloak integration");
            modified |= setIfNot(props, "hawtio.roles", "admin,user", null, null);
            modified |= setIfNot(props, "hawtio.realm", "karaf", null, null);
            modified |= setIfNot(props, "hawtio.keycloakClientConfig", "file://" + etc + "/keycloak-hawtio.json", "file://${karaf.etc}/keycloak-hawtio.json", null);
            modified |= setIfNot(props, "hawtio.rolePrincipalClasses", "org.keycloak.adapters.jaas.RolePrincipal,org.apache.karaf.jaas.boot.principal.RolePrincipal", null, null);
            if (modified) {
                props.save();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> "Error persisting system properties");
        }

        extract("keycloak-hawtio.json", Paths.get(etc, "keycloak-hawtio.json").toString());

        for (Bundle bundle : context.getBundles()) {
            String bsn = bundle.getSymbolicName();
            if ("io.hawt.hawtio-war".equals(bsn)) {
                new Thread(() -> {
                    try {
                        if (bundle.getState() == Bundle.ACTIVE) {
                            bundle.stop(Bundle.STOP_TRANSIENT);
                            bundle.start(Bundle.START_TRANSIENT | Bundle.START_ACTIVATION_POLICY);
                        }
                    } catch (BundleException e) {
                        LOGGER.log(Level.SEVERE, e, () -> "Error restarting hawtio bundle");
                    }
                }).start();
            }
        }
    }

    private void extract(String source, String destination) {
        Path p = Paths.get(destination);
        if (!Files.exists(p)) {
            LOGGER.log(Level.INFO, () -> "Extracting keycloak configuration from '" + source + "' to '" + destination + "'");
            try (InputStream is = getClass().getResourceAsStream(source)) {
                Files.copy(is, p);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e, () -> "Error extracting keycloak configuration from '" + source + "' to '" + destination + "'");
            }
        }
    }

    private boolean setIfNot(Properties props, String key, String value, String literal, String comment) {
        if (System.getProperty(key) == null) {
            props.put(key,
                      comment != null ? Collections.singletonList(comment) : Collections.emptyList(),
                      Collections.singletonList(literal != null ? literal : value));
            System.setProperty(key, value);
            return true;
        }
        return false;
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
