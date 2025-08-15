### Demonstration of a functioning Kubernetes ecosystem including
* Two Micronaut services
* Keycloak authentication
* NGINX reverse proxy
* Simple angular UI (outside of the cluster)

## Environment
This was created using Kubernetes 1.32.2 and a Docker Desktop cluster. It has not been tested in Minikube or any other cluster.

## To start
1. From the root directory run `./start_ecosystem.sh`
2. First this clears out existing deployments. On your first run you will see warnings because the deployments do not exist.
3. Next we prepare the gRPC code. We have created this as a separate project as we will want plugins to provide these similar to the api projects in use now. This will result in a publication to your local maven repository.
4. Create the Docker images for the two Micronaut services. 
   1. When I run this I get stack traces in the logs. Micronaut appears to expect a GCP login. If this appears you should be able to ignore it.
5. Build the Docker image for the reverse proxy.
6. Start up the angular UI.
   1. This does not go through Kubernetes, and is outside of the reverse proxy. I couldn't get this to work in the Kubernetes cluster, and didn't pursue it long because we don't actually need it.
   2. It is, however, a Docker image.
7.  Build the image for the Keycloak server.
      1. This will build with a realm configured for this environment.
      2. There is not a database set up for it.  
8. Finally, we apply the YAML files to create the deployments.
9. The keycloak server will be the last to finish starting.
   1. Navigate to http://localhost:8080/auth/realms/spike-realm/.well-known/openid-configuration and wait for it to show the correct OIDC document.
10. Navigate to localhost:4200 to see the angular UI.
    1. The login credentials are test-user:password

## Explanation of features
* Neither Micronaut service is exposed directly by Kubernetes. Only Backend One is proxied by nginx.
* Keycloak has been configured to be exposed by kubernetes. This is to make the admin console available for testing purposes.
  * The admin console will not be available in production, per Keycloak's security recommendations.
  * Kubernetes will assign an IP address to the Keycloak server and it changes every deployment. To access it run `kubectl get services -n spike-space` and use the IP address from the `PORT(S)` column. The admin console will be available at `http://localhost:<IP_ADDRESS>/auth`
  * Keycloak authentication by the Angular UI and Backend One are handled through the reverse proxy, as would be done in production.
* We demonstrate that we can authenticate through Kubernetes and the reverse proxy.
* We demonstrate that we can make a gRPC call to the backend service without it being exposed directly or indirectly.

