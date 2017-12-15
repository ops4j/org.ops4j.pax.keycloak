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
package org.ops4j.pax.keycloak.jaas;

import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.util.tracker.BaseActivator;
import org.apache.karaf.util.tracker.annotation.Managed;
import org.apache.karaf.util.tracker.annotation.ProvideService;
import org.apache.karaf.util.tracker.annotation.Services;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Managed("org.ops4j.pax.keycloak.jaas")
@Services(provides = {
        @ProvideService(JaasRealm.class),
})
public class Activator extends BaseActivator implements ManagedService {

    private static final String KARAF_ETC = System.getProperty("karaf.etc");

    public static final String DETAILED_LOGIN_EXCEPTION = "detailed.login.exception";

    public static final String JAAS_BEARER_KEYCLOAK_CONFIG_FILE = "jaasBearerKeycloakConfigFile";
    public static final String JAAS_BEARER_ROLE_PRINCIPAL_CLASS = "jaasBearerRolePrincipalClass";

    public static final String JAAS_DIRECT_ACCESS_KEYCLOAK_CONFIG_FILE = "jaasDirectAccessKeycloakConfigFile";
    public static final String JAAS_DIRECT_ACCESS_ROLE_PRINCIPAL_CLASS = "jaasDirectAccessRolePrincipalClass";

    public static final String AUDIT_EVENTADMIN_ENABLED = "audit.eventadmin.enabled";
    public static final String AUDIT_EVENTADMIN_TOPIC = "audit.eventadmin.topic";

    private static final Logger LOGGER = Logger.getLogger(Activator.class.getName());

    private KarafRealm karafRealm;

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();

        Map<String, Object> config = getConfig();

        extract("keycloak-bearer.json", config.get(JAAS_BEARER_KEYCLOAK_CONFIG_FILE).toString());
        extract("keycloak-direct-access.json", config.get(JAAS_DIRECT_ACCESS_KEYCLOAK_CONFIG_FILE).toString());

        karafRealm = new KarafRealm(bundleContext, config);
        register(JaasRealm.class, karafRealm);
    }

    @Override
    protected void doStop() {
        super.doStop();
    }

    @Override
    protected void reconfigure() {
        Map<String, Object> config = getConfig();
        if (karafRealm != null) {
            karafRealm.updated(config);
        }
    }

    private Map<String, Object> getConfig() {
        Path karafEtc = Paths.get(KARAF_ETC);

        Map<String, Object> config = new HashMap<>();
        populate(config, DETAILED_LOGIN_EXCEPTION, "false");
        populate(config, JAAS_BEARER_KEYCLOAK_CONFIG_FILE, karafEtc.resolve("keycloak-bearer.json").toString());
        populate(config, JAAS_BEARER_ROLE_PRINCIPAL_CLASS, "org.apache.karaf.jaas.boot.principal.RolePrincipal");
        populate(config, JAAS_DIRECT_ACCESS_KEYCLOAK_CONFIG_FILE, karafEtc.resolve("keycloak-direct-access.json").toString());
        populate(config, JAAS_DIRECT_ACCESS_ROLE_PRINCIPAL_CLASS, "org.apache.karaf.jaas.boot.principal.RolePrincipal");
        populate(config, AUDIT_EVENTADMIN_ENABLED, "true");
        populate(config, AUDIT_EVENTADMIN_TOPIC, "org/apache/karaf/login");
        config.put(BundleContext.class.getName(), bundleContext);
        return config;
    }

    private void populate(Map<String, Object> map, String key, String def) {
        map.put(key, getString(key, def));
    }

    private void extract(String source, String destination) {
        Path p = Paths.get(destination);
        if (!Files.exists(p)) {
            LOGGER.log(Level.INFO, () -> "Extracting keycloak configuration from '" + source + "' to '" + destination + "'");
            try (InputStream is = getClass().getResourceAsStream(source)) {
                Files.copy(is, p);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e, () -> "Error extracting keycloak configuration from '" + source + "' to '" + destination + "'");
            }
        }
    }

}
