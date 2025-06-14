printf "Stopping any currently running containers...\n"
docker container stop reverse-proxy
docker container stop backend-two
docker container stop backend-one
cd python-backend
docker build -t backend-one .
cd ../python-backend-two
docker build -t backend-two .
cd ../reverse-proxy
docker build -t reverse-proxy .
cd ../
docker compose up -d
sleep 1
printf "making hello request to backend one\n"
curl http://localhost:5001
printf "\n"
printf "Asking backend one to get hello from backend two\n"
curl http://localhost:5001/request
printf "\n"
printf "All requests to the 'params' will go to a context path on the first backend server, including any params\n"
printf "Sending with a path param\n"
curl http://localhost:5001/params/path/my-path-param
printf "\n"
printf "sending a request with a query param \n"
curl http://localhost:5001/params/query?myQuery=doingStuff
printf "\n"
