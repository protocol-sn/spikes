./gradlew clean bootJar
docker build -t flyway-migration:latest .
docker compose up