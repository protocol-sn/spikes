# Client credentials OAuth

## Starting the demonstration
1. Run `docker-compose up` from the `keycloak-angular-ui` spike directory. This will start Keycloak in your Docker desktop including importing a Realm and setting up the database.
2. In the `spike-client-two` directory run `./gradlew run` to start the secured resource server. 
3. In the `spike-client-one` directory run `./gradlew run` to start the client seeking to access the secured resource server.
4. On application startup the client will trigger requests to the secured resource server. You will see trace statements in the console of spike-client-one indicating it received expected responses. In the console for spike-client-two you will see micronaut-security debug output indicating authentication performing as expected.
5. In the `secure-service-ui` directory run `ng serve` to start the ui.
6. In a browser navigate to http://localhost:4200 and click login. You will be redirected to the keycloak login page. Your credentials are `test-user` and `password`. You will be redirected back to the home page which will now show that you are authenticated and will list the user info claims available to AngularJS.
7. Select the unsecure tab and click "Unsecured" button to make a call that requires no OAuth. You should get a 200 with a message from the spike-two service.
8. Select the secure tab and try each of the buttons.
	1. The "Spike" button will make a call to an endpoint that requires a role test-user does not have. You will get a 403.
	2. The "Spike2" button will make a call to an endpoint that requires a role test-user has. You should get a 200 with a message from the spike-two service.
	3. The "NoRole" button will make a call to an endpoint that requires auth but no role. You should get a 200 with a message from the spike-two service.
	4. The "workaround" button will make a call to the spike-one service that requires the same role from the "Spike2" call. The spike-one service will then make a call to the spike-two service at the same endpoint you attempted in the "Spike" button. You should get a 200 response with a message from the spike-two service that has been modified by the spike-one service.

## See also
- [AngularJS OIDC OAuth2](https://www.npmjs.com/package/angular-oauth2-oidc)
