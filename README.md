Pax Keycloak
============

Pax Keycloak aimed at providing support for [Keycloak](http://www.keycloak.org) in [Apache Karaf](https://karaf.apache.org).

## Rationale

The reason behind this project was to provide these Karaf features:

* pax-keycloak-http-jetty
* pax-keycloak-http-tomcat
* pax-keycloak-http-undertow

These features provided an integration point between pax-web and Keycloak.

## Change

Everything has changes (and became simpler) with [PAXWEB-1161](https://ops4j1.jira.com/browse/PAXWEB-1161) issue that _reverted_
the dependency between pax-web and Keycloak.

New pax-web's `org.ops4j.pax.web.service.AuthenticatorService` is now implemented by Keycloak itself and detected by pax-web using service locator pattern.

Keycloak ships 3 jars that include `/META-INF/services/org.ops4j.pax.web.service.AuthenticatorService` service:
* org.keycloak:keycloak-pax-web-jetty94
* org.keycloak:keycloak-pax-web-tomcat8
* org.keycloak:keycloak-pax-web-undertow

special pax-keycloak project is no longer needed.

Keycloak documentation related to integration with pax web is [in the section for Fuse 7 in official documentation](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter).

Examples (specific to Fuse 7, but should work on plain Karaf as well) related to pax-web and keycloak integration
[can be found here](https://github.com/jboss-fuse/karaf-quickstarts/tree/7.x.redhat-7-x/security/keycloak).

## Examples

In order to run examples, we'll use Apache Karaf 4.2.3, Keycloak 4.5.0.Final (least version where integration works without issues) and Camel 2.23.0.

In Karaf, we have to add Keycloak (use Keycloak 4.5.0.Final or newer) and Camel feature repositories:

    karaf@root()> feature:repo-add mvn:org.keycloak/keycloak-osgi-features/4.5.0.Final/xml/features
    Adding feature url mvn:org.keycloak/keycloak-osgi-features/4.5.0.Final/xml/features
    karaf@root()> feature:repo-add mvn:org.apache.camel.karaf/apache-camel/2.23.0/xml/features
    Adding feature url mvn:org.apache.camel.karaf/apache-camel/2.23.0/xml/features

Integration with Jetty and Tomcat should work as well, but we'll use Undertow:

    karaf@root()> feature:install pax-http-undertow

## Simple HttpService example

`keycloak-httpservice` example shows the most fundamental integration, where plain `javax.servlet.http.HttpServlet` instance is registered using OSGi Http Service.

After building project using `mvn clean install -Phttpservice-named`, we can install the bundle in Karaf (there's also profile `httpservice-default` - more details in [keycloak-httpservice/README.md](keycloak-httpservice/README.md):

    karaf@root()> install mvn:org.ops4j.pax.keycloak.quickstarts/keycloak-httpservice/1.0.0
    Bundle ID: 126
    karaf@root()> start 126
    karaf@root()>      

Assuming that Keycloak is running on localhost:8180 and required realm is prepared in Keycloak (to configured Keycloak itself, please refer to [official documentation](https://www.keycloak.org/docs/latest/)), where:

* realm name is `karaf`
* there's client `hs-info` Keycloak _client_ configured with:
  * standard flow enabled
  * access type: `public`
  * Valid redirect URIs: `http://localhost:8181/*`
  * Base URL: `http://localhost:8181/`
  * Web Origins: `+`
  * Role: `admin`

We have to create `${karaf.etc}/app1-keycloak.json` file with:

    {
        "realm": "karaf",
        "auth-server-url": "http://localhost:8180/auth",
        "ssl-required": "external",
        "resource": "hs-info",
        "public-client": true,
        "use-resource-role-mappings": true,
        "confidential-port": 0,
        "principal-attribute": "preferred_username"
    }

With the above configuration, we can browse to http://localhost:8181/app1/info and see a login page from Keycloak. After logging in there should be a page presented that shows details about user from Keycloak.

## Camel example

Here's an example of how to integrate pax-web, keycloak, and Camel. `keycloak-camel-blueprint` shows how
Camel integrates with keycloak and pax-web.

After building project using `mvn clean install`, we can install the bundle in Karaf (more details in [keycloak-camel-blueprint/README.md](keycloak-camel-blueprint/README.md))):

Install Camel features and `keycloak-pax-http-undertow`:

    karaf@root()> feature:install camel
    karaf@root()> feature:install camel-undertow
    karaf@root()> feature:install -v keycloak-pax-http-undertow

Now we're ready to install Camel application that uses Keycloak integration.

    karaf@root()> install mvn:org.ops4j.pax.keycloak.quickstarts/keycloak-camel-blueprint/1.0.0
    Bundle ID: 128
    karaf@root()> start 128

Assuming that Keycloak is running on localhost:8180 and required realm is prepared in Keycloak (to configured Keycloak itself, please refer to [official documentation](https://www.keycloak.org/docs/latest/)), where:

* realm name is `karaf`
* there's client `camel-undertow-endpoint` Keycloak _client_ configured with:
  * direct access grants flow enabled
  * access type: `confidential`
  * Base URL: `http://localhost:8383/`
  * Role: `admin`
  * secret: `f591a8ae-5a82-40de-9190-ea84ceca05a7`

we can run `org.ops4j.pax.keycloak.quickstarts.camel.CamelClientTest.accessCamelRoute` JUnit test that shows how to access such route programmatically. The JUnit test shows how to perform _OAuth2 dance_ to get a token and use this token to access Camel route.
