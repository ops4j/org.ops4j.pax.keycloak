Build and Deploy the Quickstart
-------------------------------

To build the quick start:

1. Change your working directory to `keycloak-camel-blueprint` directory.
2. Run `mvn clean install` to build the quickstart.
3. Start Apache Karaf by running bin/karaf (on Linux) or bin\karaf.bat (on Windows).
4. In Karaf console, enter the following commands to install required pax-web and Keycloak features

        feature:repo-add mvn:org.keycloak/keycloak-osgi-features/4.5.0.Final/xml/features
        feature:install -v pax-http-undertow
        feature:install -v keycloak-pax-http-undertow

5. In Karaf console, enter the following command to install `keycloak-camel-blueprint` quickstart:

        install -s mvn:org.ops4j.pax.keycloak/keycloak-camel-blueprint/1.0.0

6. There's no need to create a Keycloak configuration inside `etc/` directory, as with this quickstart it's embedded inside the bundle.

Use the bundle
--------------

This time we won't browse to any URL using web browser. After installing `keycloak-camel-blueprint` bundle, we'll
have a Camel route exposing an endpoint that expectes `Bearer` authentication. This means we need valid OAuth2
token to authenticate.

The quickstart includes `org.ops4j.pax.keycloak.quickstarts.camel.CamelClientTest` unit test that shows
(in Java code) the OAuth2 flow required to securely connect to and endpoint exposed by Camel route.

The unit test can be run inside `keycloak-camel-blueprint` directory:

    12:17 $ mvn test
    [INFO] Scanning for projects...
    [INFO] 
    [INFO] ----< org.ops4j.pax.keycloak.quickstarts:keycloak-camel-blueprint >-----
    [INFO] Building keycloak-camel-blueprint 1.0.0
    [INFO] -------------------------------[ bundle ]-------------------------------
    [INFO] 
    [INFO] --- maven-resources-plugin:3.0.2:resources (default-resources) @ keycloak-camel-blueprint ---
    [INFO] Using 'UTF-8' encoding to copy filtered resources.
    [INFO] Copying 2 resources
    [INFO] 
    [INFO] --- maven-compiler-plugin:3.7.0:compile (default-compile) @ keycloak-camel-blueprint ---
    [INFO] Nothing to compile - all classes are up to date
    [INFO] 
    [INFO] --- maven-bundle-plugin:3.5.1:manifest (bundle-manifest) @ keycloak-camel-blueprint ---
    [INFO] 
    [INFO] --- maven-resources-plugin:3.0.2:testResources (default-testResources) @ keycloak-camel-blueprint ---
    [INFO] Using 'UTF-8' encoding to copy filtered resources.
    [INFO] Copying 1 resource
    [INFO] 
    [INFO] --- maven-compiler-plugin:3.7.0:testCompile (default-testCompile) @ keycloak-camel-blueprint ---
    [INFO] Changes detected - recompiling the module!
    [INFO] Compiling 1 source file to /data/sources/github.com/ops4j/org.ops4j.pax.keycloak/keycloak-camel-blueprint/target/test-classes
    [INFO] 
    [INFO] --- maven-surefire-plugin:2.20.1:test (default-test) @ keycloak-camel-blueprint ---
    [INFO] 
    [INFO] -------------------------------------------------------
    [INFO]  T E S T S
    [INFO] -------------------------------------------------------
    [INFO] Running org.ops4j.pax.keycloak.quickstarts.camel.CamelClientTest
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "POST /auth/realms/karaf/protocol/openid-connect/token HTTP/1.1[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "Authorization: Basic Y2FtZWwtdW5kZXJ0b3ctZW5kcG9pbnQ6ZjU5MWE4YWUtNWE4Mi00MGRlLTkxOTAtZWE4NGNlY2EwNWE3[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "Content-Length: 52[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "Content-Type: application/x-www-form-urlencoded[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "Host: localhost:8180[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "User-Agent: Apache-HttpClient/4.5.6 (Java/1.8.0_202)[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 >> "grant_type=password&username=admin&password=passw0rd"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "Connection: keep-alive[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "Cache-Control: no-store[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "Set-Cookie: KC_RESTART=; Version=1; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Max-Age=0; Path=/auth/realms/karaf/; HttpOnly[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "Pragma: no-cache[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "Content-Type: application/json[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "Content-Length: 3571[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "Date: Mon, 25 Feb 2019 11:19:13 GMT[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-0 << "{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJsMHg0amRod3pWWkprMmd1T2Uxai1NblNwbTUyMFpHazlpTDBzaF92bFhvIn0.eyJqdGkiOiI2NGVkZjYxYS1mNzc1LTRlYTctOGNmOC04MTc1OGVjZDNlOTAiLCJleHAiOjE1NTEwOTM4NTMsIm5iZiI6MCwiaWF0IjoxNTUxMDkzNTUzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvZnVzZTdrYXJhZiIsImF1ZCI6ImNhbWVsLXVuZGVydG93LWVuZHBvaW50Iiwic3ViIjoiZDg4Njc1YTQtMWIzMi00ZjhhLWJiYTUtMWNmMzFiMWVmNTAwIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2FtZWwtdW5kZXJ0b3ctZW5kcG9pbnQiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiIwMTg0YTNjYy1kNmUxLTRmYWMtODFkMC0yNDU3Mzg2MTVlYmYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIiJdLCJyZXNvdXJjZV9hY2Nlc3MiOnsiY3hmIjp7InJvbGVzIjpbImFkbWluIl19LCJocy1pbmZvIjp7InJvbGVzIjpbImFkbWluIl19LCJjYW1lbC11bmRlcnRvdy1lbmRwb2ludCI6eyJyb2xlcyI6WyJhZG1pbiJdfSwia2V5Y2xvYWstd2FyIjp7InJvbGVzIjpbImFkbWluIl19LCJzc2giOnsicm9sZXMiOlsidmlld2VyIiwibWFuYWdlciIsInNzaCIsImFkbWluIiwic3lzdGVtYnVuZGxlcyJdfSwiaGF3dGlvLXNlcnZlciI6eyJyb2xlcyI6WyJ2aWV3ZXIiLCJtYW5hZ2VyIiwiYWRtaW4iLCJzc2giLCJzeXN0ZW1idW5kbGVzIl19LCJjeGYtZXh0ZXJuYWwiOnsicm9sZXMiOlsiYWRtaW4iXX0sImNhbWVsLXVuZGVydG93LXJlc3Rkc2wtZW5kcG9pbnQiOnsicm9sZXMiOlsiYWRtaW4iXX0sIndoaXRlYm9hcmQtYmx1ZXByaW50LWluZm8iOnsicm9sZXMiOlsiYWRtaW4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfSwid2hpdGVib2FyZC1pbmZvIjp7InJvbGVzIjpbImFkbWluIl19LCJocy1ibHVlcHJpbnQtaW5mbyI6eyJyb2xlcyI6WyJhZG1pbiJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW4iLCJnaXZlbl9uYW1lIjoiSm9obiIsImZhbWlseV9uYW1lIjoiRG9lIiwiZW1haWwiOiJhZG1pbkByZWRoYXQuY29tIn0.Yh1CcVExwIWl11ccjJXmphaTVT1OdzLcb8U5BiV2NjkPYYhYkhg4FavTFaszYcNZqWqdqrml2MPqwujgs0wXradsT6KwO12QUEnMQ47wIbiSZ34Pafok0_rggUrkV_4dh3nPSYWMxNRq2xK_rubOuFov1SR_H1hYuGBEdPN_ZFevHyoHgjKjjzIPlzQdhSmB6HoroMoC4gMhHRP-9rPotQca_Dv9aOwjVhf6XOQ3NDqgBI5zrqHZD67MR_qmcpv_YKrG_Cll7zfU6yEkcBhIhwqsQ5QZAyaUNw72B7XYVgTBNUmaELsmfoud4A6kw1vro9xYS_AhCLx8C5Q8_nJwGw","expires_in":300,"refresh_expires_in":1800,"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3ZTc3Nzc2Mi1iZjcyLTQyOWUtOTc0My05ODNhZDIwOGIyZDMifQ.eyJqdGkiOiJjNTQxOWNmMS1iY2MyLTQ5YmItYjU4ZC01NGQ0Mjk4NzlhYzIiLCJleHAiOjE1NTEwOTUzNTMsIm5iZiI6MCwiaWF0IjoxNTUxMDkzNTUzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvZnVzZTdrYXJhZiIsImF1ZCI6ImNhbWVsLXVuZGVydG93LWVuZHBvaW50Iiwic3ViIjoiZDg4Njc1YTQtMWIzMi00ZjhhLWJiYTUtMWNmMzFiMWVmNTAwIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImNhbWVsLXVuZGVydG93LWVuZHBvaW50IiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiMDE4NGEzY2MtZDZlMS00ZmFjLTgxZDAtMjQ1NzM4NjE1ZWJmIiwicmVzb3VyY2VfYWNjZXNzIjp7ImN4ZiI6eyJyb2xlcyI6WyJhZG1pbiJdfSwiaHMtaW5mbyI6eyJyb2xlcyI6WyJhZG1pbiJdfSwiY2FtZWwtdW5kZXJ0b3ctZW5kcG9pbnQiOnsicm9sZXMiOlsiYWRtaW4iXX0sImtleWNsb2FrLXdhciI6eyJyb2xlcyI6WyJhZG1pbiJdfSwic3NoIjp7InJvbGVzIjpbInZpZXdlciIsIm1hbmFnZXIiLCJzc2giLCJhZG1pbiIsInN5c3RlbWJ1bmRsZXMiXX0sImhhd3Rpby1zZXJ2ZXIiOnsicm9sZXMiOlsidmlld2VyIiwibWFuYWdlciIsImFkbWluIiwic3NoIiwic3lzdGVtYnVuZGxlcyJdfSwiY3hmLWV4dGVybmFsIjp7InJvbGVzIjpbImFkbWluIl19LCJjYW1lbC11bmRlcnRvdy1yZXN0ZHNsLWVuZHBvaW50Ijp7InJvbGVzIjpbImFkbWluIl19LCJ3aGl0ZWJvYXJkLWJsdWVwcmludC1pbmZvIjp7InJvbGVzIjpbImFkbWluIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX0sIndoaXRlYm9hcmQtaW5mbyI6eyJyb2xlcyI6WyJhZG1pbiJdfSwiaHMtYmx1ZXByaW50LWluZm8iOnsicm9sZXMiOlsiYWRtaW4iXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwifQ.iNi2aBwt9ZePWh8OSv82amS_kRhktkmL7FZnOAyv5-M","token_type":"bearer","not-before-policy":0,"session_state":"0184a3cc-d6e1-4fac-81d0-245738615ebf","scope":"profile email"}"
    12:19:13 INFO [org.ops4j.pax.keycloak.quickstarts.camel.CamelClientTest] : token: eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJsMHg0amRod3pWWkprMmd1T2Uxai1NblNwbTUyMFpHazlpTDBzaF92bFhvIn0.eyJqdGkiOiI2NGVkZjYxYS1mNzc1LTRlYTctOGNmOC04MTc1OGVjZDNlOTAiLCJleHAiOjE1NTEwOTM4NTMsIm5iZiI6MCwiaWF0IjoxNTUxMDkzNTUzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvZnVzZTdrYXJhZiIsImF1ZCI6ImNhbWVsLXVuZGVydG93LWVuZHBvaW50Iiwic3ViIjoiZDg4Njc1YTQtMWIzMi00ZjhhLWJiYTUtMWNmMzFiMWVmNTAwIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2FtZWwtdW5kZXJ0b3ctZW5kcG9pbnQiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiIwMTg0YTNjYy1kNmUxLTRmYWMtODFkMC0yNDU3Mzg2MTVlYmYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIiJdLCJyZXNvdXJjZV9hY2Nlc3MiOnsiY3hmIjp7InJvbGVzIjpbImFkbWluIl19LCJocy1pbmZvIjp7InJvbGVzIjpbImFkbWluIl19LCJjYW1lbC11bmRlcnRvdy1lbmRwb2ludCI6eyJyb2xlcyI6WyJhZG1pbiJdfSwia2V5Y2xvYWstd2FyIjp7InJvbGVzIjpbImFkbWluIl19LCJzc2giOnsicm9sZXMiOlsidmlld2VyIiwibWFuYWdlciIsInNzaCIsImFkbWluIiwic3lzdGVtYnVuZGxlcyJdfSwiaGF3dGlvLXNlcnZlciI6eyJyb2xlcyI6WyJ2aWV3ZXIiLCJtYW5hZ2VyIiwiYWRtaW4iLCJzc2giLCJzeXN0ZW1idW5kbGVzIl19LCJjeGYtZXh0ZXJuYWwiOnsicm9sZXMiOlsiYWRtaW4iXX0sImNhbWVsLXVuZGVydG93LXJlc3Rkc2wtZW5kcG9pbnQiOnsicm9sZXMiOlsiYWRtaW4iXX0sIndoaXRlYm9hcmQtYmx1ZXByaW50LWluZm8iOnsicm9sZXMiOlsiYWRtaW4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfSwid2hpdGVib2FyZC1pbmZvIjp7InJvbGVzIjpbImFkbWluIl19LCJocy1ibHVlcHJpbnQtaW5mbyI6eyJyb2xlcyI6WyJhZG1pbiJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW4iLCJnaXZlbl9uYW1lIjoiSm9obiIsImZhbWlseV9uYW1lIjoiRG9lIiwiZW1haWwiOiJhZG1pbkByZWRoYXQuY29tIn0.Yh1CcVExwIWl11ccjJXmphaTVT1OdzLcb8U5BiV2NjkPYYhYkhg4FavTFaszYcNZqWqdqrml2MPqwujgs0wXradsT6KwO12QUEnMQ47wIbiSZ34Pafok0_rggUrkV_4dh3nPSYWMxNRq2xK_rubOuFov1SR_H1hYuGBEdPN_ZFevHyoHgjKjjzIPlzQdhSmB6HoroMoC4gMhHRP-9rPotQca_Dv9aOwjVhf6XOQ3NDqgBI5zrqHZD67MR_qmcpv_YKrG_Cll7zfU6yEkcBhIhwqsQ5QZAyaUNw72B7XYVgTBNUmaELsmfoud4A6kw1vro9xYS_AhCLx8C5Q8_nJwGw
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 >> "GET /admin-camel-endpoint HTTP/1.1[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 >> "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJsMHg0amRod3pWWkprMmd1T2Uxai1NblNwbTUyMFpHazlpTDBzaF92bFhvIn0.eyJqdGkiOiI2NGVkZjYxYS1mNzc1LTRlYTctOGNmOC04MTc1OGVjZDNlOTAiLCJleHAiOjE1NTEwOTM4NTMsIm5iZiI6MCwiaWF0IjoxNTUxMDkzNTUzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvZnVzZTdrYXJhZiIsImF1ZCI6ImNhbWVsLXVuZGVydG93LWVuZHBvaW50Iiwic3ViIjoiZDg4Njc1YTQtMWIzMi00ZjhhLWJiYTUtMWNmMzFiMWVmNTAwIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2FtZWwtdW5kZXJ0b3ctZW5kcG9pbnQiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiIwMTg0YTNjYy1kNmUxLTRmYWMtODFkMC0yNDU3Mzg2MTVlYmYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIiJdLCJyZXNvdXJjZV9hY2Nlc3MiOnsiY3hmIjp7InJvbGVzIjpbImFkbWluIl19LCJocy1pbmZvIjp7InJvbGVzIjpbImFkbWluIl19LCJjYW1lbC11bmRlcnRvdy1lbmRwb2ludCI6eyJyb2xlcyI6WyJhZG1pbiJdfSwia2V5Y2xvYWstd2FyIjp7InJvbGVzIjpbImFkbWluIl19LCJzc2giOnsicm9sZXMiOlsidmlld2VyIiwibWFuYWdlciIsInNzaCIsImFkbWluIiwic3lzdGVtYnVuZGxlcyJdfSwiaGF3dGlvLXNlcnZlciI6eyJyb2xlcyI6WyJ2aWV3ZXIiLCJtYW5hZ2VyIiwiYWRtaW4iLCJzc2giLCJzeXN0ZW1idW5kbGVzIl19LCJjeGYtZXh0ZXJuYWwiOnsicm9sZXMiOlsiYWRtaW4iXX0sImNhbWVsLXVuZGVydG93LXJlc3Rkc2wtZW5kcG9pbnQiOnsicm9sZXMiOlsiYWRtaW4iXX0sIndoaXRlYm9hcmQtYmx1ZXByaW50LWluZm8iOnsicm9sZXMiOlsiYWRtaW4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfSwid2hpdGVib2FyZC1pbmZvIjp7InJvbGVzIjpbImFkbWluIl19LCJocy1ibHVlcHJpbnQtaW5mbyI6eyJyb2xlcyI6WyJhZG1pbiJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW4iLCJnaXZlbl9uYW1lIjoiSm9obiIsImZhbWlseV9uYW1lIjoiRG9lIiwiZW1haWwiOiJhZG1pbkByZWRoYXQuY29tIn0.Yh1CcVExwIWl11ccjJXmphaTVT1OdzLcb8U5BiV2NjkPYYhYkhg4FavTFaszYcNZqWqdqrml2MPqwujgs0wXradsT6KwO12QUEnMQ47wIbiSZ34Pafok0_rggUrkV_4dh3nPSYWMxNRq2xK_rubOuFov1SR_H1hYuGBEdPN_ZFevHyoHgjKjjzIPlzQdhSmB6HoroMoC4gMhHRP-9rPotQca_Dv9aOwjVhf6XOQ3NDqgBI5zrqHZD67MR_qmcpv_YKrG_Cll7zfU6yEkcBhIhwqsQ5QZAyaUNw72B7XYVgTBNUmaELsmfoud4A6kw1vro9xYS_AhCLx8C5Q8_nJwGw[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 >> "Host: localhost:8383[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 >> "Connection: Keep-Alive[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 >> "User-Agent: Apache-HttpClient/4.5.6 (Java/1.8.0_202)[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 >> "[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 << "HTTP/1.1 200 OK[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 << "Connection: keep-alive[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 << "Content-Length: 40[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 << "Date: Mon, 25 Feb 2019 11:19:13 GMT[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 << "[\r][\n]"
    12:19:13 DEBUG [org.apache.http.wire] : http-outgoing-1 << "Hello admin! Your full name is John Doe."
    12:19:13 INFO [org.ops4j.pax.keycloak.quickstarts.camel.CamelClientTest] : response: Hello admin! Your full name is John Doe.
    [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.858 s - in org.ops4j.pax.keycloak.quickstarts.camel.CamelClientTest
    [INFO] 
    [INFO] Results:
    [INFO] 
    [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
    [INFO] 
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  3.918 s
    [INFO] Finished at: 2019-02-25T12:19:13+01:00
    [INFO] ------------------------------------------------------------------------
