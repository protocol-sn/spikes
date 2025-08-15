docker stop angular-ui
docker rm angular-ui
docker image rm angular-ui
kubectl delete -f deployment.yml
docker image rm backend-one
docker image rm backend-two
docker image rm reverse-proxy
docker image rm keycloak-auth
