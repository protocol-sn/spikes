## Using an NGINX server as a reverse proxy to occlude other services

### Explanation
In this demo we have two services. Each has a simple "hello" at `/` and one has an endpoint that will return the response from the other's hello endpoint.
We also have an nginx server.
These have dockerfiles that will create an image for each and a compose file that will mount and run these images into containers.
To see the process run execute the `run-demo.sh` script.
After observing the script out put note that the only container with exposed ports is the reverse proxy.

### Steps taken
1. The three components are built as docker images
2. The three images are mounted as containers on a network
3. A series of curl commands make requests of the first backend server
	1. The first gets its hello response
	2. The second has it make a request to the second server, returning its hello response
	3. The third is a prefix proxy demonstrating that the context path beyond the prefixed portion, including path params, will be carried on to the proxied server
	4. The fourth is the same as the third, demonstrating (probably unnecessarily) that a query param will also be passed on
	
### caveats
1. Use of alternate virtual hosts, including subdomains, is not tested here, as we only have localhost. 
2. The urls all use docker container names for the host. Will this demo work on other platforms?
