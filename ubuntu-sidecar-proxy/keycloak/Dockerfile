FROM ubuntu:25.04

RUN apt-get -y update

#Install the thing used for downloading
RUN apt install wget -y

#Install nginx proxy
RUN apt-get -y install nginx
COPY default /etc/nginx/sites-available/default

#Install JDK
RUN apt-get install default-jdk -y

#Install Keycloak
RUN wget -O keycloak.tar.gz https://github.com/keycloak/keycloak/releases/download/26.3.0/keycloak-26.3.0.tar.gz
RUN mkdir keycloak
RUN tar -xzvf keycloak.tar.gz --directory keycloak keycloak-26.3.0 --strip-components=1
RUN rm keycloak.tar.gz
COPY ecosystem.json keycloak/data/import/ecosystem.json

#Create user group for Keycloak
#RUN groupadd keycloak
#RUN useradd -r -g keycloak -d /keycloak -s /sbin/nologin keycloak
#RUN chown -R keycloak: keycloak
#RUN chmod o+x keycloak/bin

#Set docker env vars
ENV KC_METRICS_ENABLED=true
ENV KC_HOSTNAME_STRICT=false
ENV KC_HTTP_ENABLED=true
ENV PROXY_ADDRESS_FORWARDING=true
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin
ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENV KC_PROXY_HEADERS=xforwarded
ENV KC_HTTP_RELATIVE_PATH=/auth
ENV KC_HEALTH_ENABLED=true
ENV KC_DB_URL_HOST=db
ENV KC_DB_URL_PORT=5432
ENV KC_DB_URL_DATABASE=keycloak
ENV KC_HOSTNAME=http://localhost:8080
ENV KC_HOSTNAME_ADMIN=http://localhost:8080

EXPOSE 80/tcp

CMD ["/usr/sbin/nginx", "-g", "daemon off;"]
