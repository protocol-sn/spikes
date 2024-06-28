#./gradlew clean shadowJar
docker build -t spike-client-two:latest .
docker compose up --detach