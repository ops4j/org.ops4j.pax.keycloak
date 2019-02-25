/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ops4j.pax.keycloak.quickstarts.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;

public class CamelHelloProcessor implements org.apache.camel.Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        KeycloakPrincipal keycloakPrincipal = exchange.getProperty(KeycloakPrincipal.class.getName(), KeycloakPrincipal.class);

        AccessToken accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        String fullName = accessToken.getName();

        exchange.getOut().setBody("Hello " + username + "! Your full name is " + fullName + ".");
    }

}
