# Running Keycloak in an ubuntu container

### Running locally
1. In your `/etc/hosts` file add `127.0.0.1 keycloak.psn.local`
    1. This is to ensure that the keycloak host is the same for the host and the other Docker containers
    2. Note in the compose.yaml file that the containers have the extra host `keycloak.psn.local=172.17.0.1`. 172.17.0.1 is "host.docker.internal"
2. Run `./build_spike_ecosystem.sh`
3. Wait for the ecosystem to start.
4. In the keycloak-auth container navigate to the exec tab
5. Enter `./keycloak/bin/kc.sh start --import-realm`
6. Wait for it to finish running
7. In your browser enter `http://keycloak.psn.local:9080/auth/realms/spike-realm/.well-known/openid-configuration`
    1. If this loads up an OIDC config page your keycloak is running correctly.
8. In your browser enter `http://keycloak.psn.local:5005/auth/realms/spike-realm/.well-known/openid-configuration`
    1. If this loads up an OIDC config page we know your reverse proxy is set up correctly
9. In your browser enter `http://localhost:4200`
11. Log in with "test-user:password"
12. Try out each endpoint. They should now all succeed.

### Running in prod
1. In backend-one
    1. ./gradlew clean assemble
    2. fly launch --flycast
    3. import the toml config and configure as desired
2. In backend-two
    1. ./gradlew clean assemble
    2. fly launch --flycast
    3. import the toml config and configure as desired
3. In keycloak
    1. fly launch --flycast
    2. import the toml config and configure as desired
    3. `fly ssh console` into the keycloak app and run `./keycloak/bin/kc.sh start --import realm`. This may take a while to finish
4. In reverse-proxy
    1. fly launch 
    2. import the toml config and configure as desired
5. In secure-service-ui
    1. fly launch 
    2. import the toml config and configure as desired
6. In a browser navigate to https://secure-service-ui.fly.dev
    1. UI should work exactly as in local
7. Set up wireguard proxy
    1. https://fly.io/docs/blueprints/connect-private-network-wireguard/
8. Activate your wireguard tunnel
9. In a tunnel navigate to http://psn-keycloak.internal:8080/auth/admin/master/console
10. Log in
11. Console will fail to load :-(

Although this attempt to tunnel to the admin console has failed we suspect that proper use of proxying and the KC_HOSTNAME_ADMIN config may allow this to work but we are beyond what time we want to set aside for this.
