echo undeploying...
./clean_ecosystem.sh
echo preparing grpc lib
cd GrpcSharedLib
./gradlew clean build publishToMavenLocal
cd ../
echo preparing backend-one
cd backend-one
./gradlew clean dockerBuild
cd build/docker/main
docker build . -t backend-one
cd ../../../
echo preparing backend-two
cd ../backend-two
./gradlew clean dockerBuild
cd build/docker/main
docker build . -t backend-two
cd ../../../
echo preparing reverse-proxy
cd ../reverse-proxy
docker build . -t reverse-proxy
echo starting UI
cd ../secure-service-ui
docker build . -t angular-ui
docker run -p 4200:4200 -d --name angular-ui angular-ui
cd ../
echo preparing keycloak
docker build . -t  keycloak-auth
echo starting kubernetes
kubectl apply -f deployment.yml

