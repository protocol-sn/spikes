cd GrpcSharedLib
./gradlew clean publishToMavenLocal

cd ../backend-one
./gradlew clean assemble
docker build . -t backend-one

cd ../backend-two
./gradlew clean assemble
docker build . -t backend-two

cd ../keycloak
docker build . -t keycloak-auth

cd ../reverse-proxy
docker build . -t spike-proxy

cd ../
docker compose up -d

cd secure-service-ui
docker build . -t secure-ui
docker run -p 4200:80 -d secure-ui
