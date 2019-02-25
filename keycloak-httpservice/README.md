Build and Deploy the Quickstart
-------------------------------

To build the quick start:

1. Change your working directory to `keycloak-httpservice` directory.
2. Run `mvn clean install -P<profile-name>` to build the quickstart. As `<profile-name>` you can use one of:

    * `httpservice-default` - the servlet will be registered in default context (`/`)
    * `httpservice-named` - the servlet will be registered in named context (`/app1`)

3. Start Apache Karaf by running bin/karaf (on Linux) or bin\karaf.bat (on Windows).
4. In Karaf console, enter the following commands to install required pax-web and Keycloak features

        feature:repo-add mvn:org.keycloak/keycloak-osgi-features/4.5.0.Final/xml/features
        feature:install -v pax-http-undertow
        feature:install -v keycloak-pax-http-undertow

5. In Karaf console, enter the following command to install `keycloak-httpservice` quickstart:

        install -s mvn:org.ops4j.pax.keycloak/keycloak-httpservice/1.0.0

6. Inside Karaf directory, create file `etc/info-keycloak.json` **and** `etc/logout-keycloak.json`
(when using `httpservice-default` profile) or just `etc/app1-keycloak.json` (when using `httpservice-named` profile)
with the following content configuring Keycloak integration:

        {
          "realm": "<keycloak realm name>",
          "auth-server-url": "http://localhost:8180/auth",
          "ssl-required": "external",
          "resource": "hs-info",
          "public-client": true,
          "use-resource-role-mappings": true,
          "confidential-port": 0,
          "principal-attribute": "preferred_username"
        }

    The reason for a need to have both `etc/info-keycloak.json` and `etc/logout-keycloak.json` files is that when
    servlets are registered in default context (`/`), first path segment is used to distinguish between Keycloak
    configurations.


Use the bundle
--------------

We can simply browse to:

* http://localhost:8181/info URL when using `httpservice-default` profile
* http://localhost:8181/app1/info URL when using `httpservice-named` profile

We will be redirected to Keycloak UI to perform authentication and when it succeeds, we'll get back to initial URL.
