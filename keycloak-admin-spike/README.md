## Setting up the client:
1. Start up the local keycloak
   1. cd `/keycloak`, `docker-compose up`
2. Navigate to Keycloak admin console (https://localhost:9080)
3. Login with admin/admin
4. Create the audience client scope
   1. Go to "Client scopes" on the left menu
   2. "Create client scope"
   3. Give it a name something like "my-audience" and click save
   4. Select your newly made scope and go to the "Mappers" tab
   5. Add mapper>by configuration
   6. Select "audience"
      1. Give any name as appropriate
      2. Choose the client we are interested in, `spike-client`
      3. "Included Custom Audience" must include "security-admin-console"
      4. Save
5. Create a new client
   1. Match the name in the application.yaml, `spike-client`
   2. Generate a client secret and copy it into the application.yaml
   3. Ensure "Service account roles" is enabled
   4. Under your created client go to the "Client scopes" tab and add your audience scope created above
   5. Go to the "Service account roles" tab
   6. In addition to the default roles add `manage-users`, `view-users`, `query-users`, `view-groups`, `query-groups`

To test this run 
```
curl \
  -d "client_id=spike-client" \
  -d "client_secret=2vNmrw3E22K99pso1gi02D9SJJOQP54m" \
  -d "grant_type=client_credentials" \
  "http://localhost:9080/realms/master/protocol/openid-connect/token"
```

Copy the JWT from the access token. If looked at in a JWT reader such as JWT.io you should see your roles
This can be used as a bearer token e.g.

```
curl --location 'http://localhost:9080/admin/realms/master/users/' \
--header 'Authorization: Bearer YOUR_JWT_HERE'
```

## keycloak events

Keycloak offers only logging and email for event management out of the box, but you can add custom events. 

See [keycloak events](https://github.com/cevheri/keycloak-custom-event-listener/) for more details

## Managing users

the POST and PUT endpoint for creating users is `/admin/realms/{realm}/users`. These can be used to apply attributes and roles to a user. 