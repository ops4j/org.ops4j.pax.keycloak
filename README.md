Pax Keycloak
============

Pax Keycloak aims at providing support for [Keycloak](http://www.keycloak.org) in [Apache Karaf](https://karaf.apache.org).

## Build

You'll need a machine with Java 8 and Apache Maven 3 installed.

Checkout:

    git clone git://github.com/ops4j/org.ops4j.pax.keycloak.git

Run Build:

    mvn clean install


## Releases

There is no release at this time but they willbe found in [Maven Central](https://repo1.maven.org/maven2/org/ops4j/pax/keycloak).

## Karaf Deployment

Pax-Keycloak provides features to easily integrate in Apache Karaf.
```
> repo-add mvn:org.ops4j.pax.keycloak/pax-keycloak-features/1.0.0-SNAPSHOT/xml/features
> feature:install pax-keycloak
```

