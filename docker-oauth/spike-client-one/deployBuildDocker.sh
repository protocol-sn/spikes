#./gradlew clean shadowJar
docker build -t spike-client-one:latest .
docker compose up --detach