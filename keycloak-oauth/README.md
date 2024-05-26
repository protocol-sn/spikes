# Client credentials OAuth

## Starting the demonstration
1. Run `docker-compose up` from the `keycloak-oauth` spike directory. This will start Keycloak in your Docker desktop including importing a Realm and setting up the database.
2. In the `spike-client-two` directory run `./gradlew run` to start the secured resource server. 
3. In the `spike-client-one` directory run `./gradlew run` to start the client seeking to access the secured resource server.
4. On application startup the client will trigger requests to the secured resource server. You will see trace statements in the console of spike-client-one indicating it received expected responses. In the console for spike-client-two you will see micronaut-security debug output indicating authentication performing as expected.

## Investigating what happened
1. The keycloak admin console will be available at `localhost:9080`. The login and password are both "admin".
   1. In the realm dropdown you should be able to see both the default "master" realm and the "spike-realm" that was imported. Select the spike-realm.
   2. In `clients` you will be able to see "spike-client-one" and "spike-client-two" along with various default clients.
   3. Drill down into "spike-client-one" and navigate to the "Service account roles" tab. You will see the "spike-one" role. This was created in the "Realm roles" page.
2. Investigating the code for spike-client-two, the secured resource server.
   1. Take note of the secured controller. The @Secured annotation is used as described in Micronaut documentation. Note that the valid role endpoint has the "spike-one" role we outlined before, while another has a role we did not give the client.
   2. Also note the KeycloakRoleFinder class. Keycloak provides roles in a different way than anticipated by Micronaut's default role finder. The implementation provided here is not robust, but demonstrates a naive solution to finding keycloak's roles.
3. Investigating the code for spike-client-one there is a bit less of interest.
   1. The startup listener, that contains the bulk of our logic, contains a low-level client and a commented out declarative client.
   2. Currently references to the declarative client are commented out. Trading out what is commented out will not impact the result.

## See also
- [Micronaut Client Credentials guide](https://guides.micronaut.io/latest/micronaut-oauth2-client-credentials-auth0-gradle-java.html)
- [Micronaut Keycloak guide](https://guides.micronaut.io/latest/micronaut-oauth2-keycloak-gradle-java.html)