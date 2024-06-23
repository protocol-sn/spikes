./gradlew clean shadowJar
docker build -t spike-one:latest .
docker compose up --detach