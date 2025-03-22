## How to run

1. Import the postman collection to your Postman 
2. Start the keycloak server
   1. From the project root `docker compose up`
3. Start the Micronaut server
   1. From the project root `./gradlew run`
   2. Alternatively run the Application job from IntelliJ
4. Open the postman collection and run the `user login` request
   1. You should get the access token in the response
5. Open the postman collection and run the `userinfo` request
   1. This will run against keycloak's userinfo endpoint.
   2. You will get back an object with `sub`, `email_verified`, and `preferred_email`.
6. Open the postman collection and run the `get user info` request
   1. This will run against our Micronaut service.
   2. You will get back an object with `sub`, `emailVerified`, and `preferredEmail`.

## What happened?

Micronaut's SecurityService gives info about the requesting user, but not the user's access token. We can read this directly from the Authorization header but this will not work with cookie-based auth. The `micronaut.TOKEN` request attribute *should* work in all cases, so we can capture that and use it to make the userinfo request.