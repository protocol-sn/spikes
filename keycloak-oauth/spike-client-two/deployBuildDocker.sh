./gradlew clean shadowJar
docker build -t spike-two:latest .
docker compose up --detach