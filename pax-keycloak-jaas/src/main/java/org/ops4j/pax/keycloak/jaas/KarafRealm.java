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

import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.config.JaasRealm;
import org.osgi.framework.BundleContext;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.HashMap;
import java.util.Map;

public class KarafRealm implements JaasRealm {

    private static final String REALM = "karaf";
    private static final String BEARER_MODULE = "org.keycloak.adapters.jaas.BearerTokenLoginModule";
    private static final String DIRECT_MODULE = "org.keycloak.adapters.jaas.DirectAccessGrantsLoginModule";
    private static final String EVENTADMIN_AUDIT_MODULE = "org.apache.karaf.jaas.modules.audit.EventAdminAuditLoginModule";

    private final BundleContext bundleContext;
    private volatile Map<String, Object> properties;

    public KarafRealm(BundleContext bundleContext, Map<String, Object> properties) {
        this.bundleContext = bundleContext;
        updated(properties);
    }

    public void updated(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String getName() {
        return REALM;
    }

    @Override
    public int getRank() {
        return 100;
    }

    @Override
    public AppConfigurationEntry[] getEntries() {
        Map<String, Object> bearerOptions = new HashMap<>();
        bearerOptions.put(BundleContext.class.getName(), bundleContext);
        bearerOptions.put(ProxyLoginModule.PROPERTY_MODULE, BEARER_MODULE);
        bearerOptions.put(ProxyLoginModule.PROPERTY_BUNDLE, Long.toString(bundleContext.getBundle().getBundleId()));
        bearerOptions.put("keycloak-config-file", properties.get("jaasBearerKeycloakConfigFile"));
        bearerOptions.put("role-principal-class", properties.get("jaasBearerRolePrincipalClass"));

        Map<String, Object> directKeyOptions = new HashMap<>();
        directKeyOptions.put(BundleContext.class.getName(), bundleContext);
        directKeyOptions.put(ProxyLoginModule.PROPERTY_MODULE, DIRECT_MODULE);
        directKeyOptions.put(ProxyLoginModule.PROPERTY_BUNDLE, Long.toString(bundleContext.getBundle().getBundleId()));
        directKeyOptions.put("keycloak-config-file", properties.get("jaasDirectAccessKeycloakConfigFile"));
        directKeyOptions.put("role-principal-class", properties.get("jaasDirectAccessRolePrincipalClass"));

        Map<String, Object> eventadminOptions = new HashMap<>();
        eventadminOptions.putAll(properties);
        eventadminOptions.put(BundleContext.class.getName(), bundleContext);
        eventadminOptions.put(ProxyLoginModule.PROPERTY_MODULE, EVENTADMIN_AUDIT_MODULE);
        eventadminOptions.put(ProxyLoginModule.PROPERTY_BUNDLE, Long.toString(bundleContext.getBundle().getBundleId()));
        eventadminOptions.put("enabled", properties.get("audit.eventadmin.enabled"));
        eventadminOptions.put("topic", properties.get("audit.eventadmin.topic"));

        return new AppConfigurationEntry[] {
                new AppConfigurationEntry(ProxyLoginModule.class.getName(), AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL, bearerOptions),
                new AppConfigurationEntry(ProxyLoginModule.class.getName(), AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL, directKeyOptions),
                new AppConfigurationEntry(ProxyLoginModule.class.getName(), AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL, eventadminOptions)
        };
    }

}
