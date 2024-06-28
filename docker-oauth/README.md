# Docker OAuth

## Running with a local microservice (the way that works)
1. Run `docker-compose up` from the `docker-oauth` spike directory. This will start Keycloak in your Docker desktop including importing a Realm and setting up the database.
2. Create a test user.
   1. Navigate to `http://localhost:9080` and admin with "admin:admin"
   2. Select "spike-realm"
   3. Go to "users">"add user" and provide choose a name and password for your user.
3. In the `spike-client-two` directory run `./deployBuildDocker.sh` to start the secured resource server.
   1. This service does not use a standard flow, so it can run in Docker without problems. 
3. In the `spike-client-one` directory run `./gradlew run` to start the client seeking to access the secured resource server.
4. In a browser navigate to `http://localhost:8081/trigger` to trigger a call to the secured resource server. You should see a JSON response with smoke test responses from spike-client-two.
   1. This shows us that the client_credentials flow is working.
5. Now navigate to `http://localhost:8081` and click "enter". You will be redirected to Keycloak. You can login with "demo-user:password".
   1. This shows us the standard flow is working.

## Running with all microservices in Docker
1. Terminate the current run of `spike-client-one`.
2. In the `spike-client-one` directory run `./deployBuildDocker.sh`. It is now running in Docker instead of the host machine 
3. In a browser navigate to `http://localhost:8081/trigger` to trigger a call to the secured resource server. You should see a JSON response with smoke test responses from spike-client-two.
   1. This shows us that the client_credentials flow is working.
4. Now navigate to `http://localhost:8081` and click "enter". You will be redirected to `keycloak:9080` in your browser. 
   1. The browser cannot resolve this, and you will not reach the login page 

## See also
- [Micronaut Client Credentials guide](https://guides.micronaut.io/latest/micronaut-oauth2-client-credentials-auth0-gradle-java.html)
- [Micronaut Keycloak guide](https://guides.micronaut.io/latest/micronaut-oauth2-keycloak-gradle-java.html)