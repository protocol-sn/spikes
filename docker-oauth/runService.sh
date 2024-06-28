cd spike-client-one
docker build -t spike-client-one .
cd ../spike-client-two
docker build -t spike-client-two .
cd ../
docker compose up -d
