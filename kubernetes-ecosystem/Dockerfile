FROM quay.io/keycloak/keycloak:25.0

ENV KC_METRICS_ENABLED=true
ENV KC_HOSTNAME_STRICT=false
ENV KC_HTTP_ENABLED=true
#ENV KC_PROXY="edge"
ENV PROXY_ADDRESS_FORWARDING=true
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin
#ENV KC_DB=postgres
#ENV KC_DB_URL=jdbc:postgresql://db:5432/
#ENV KC_DB_USERNAME=postgres
#ENV KC_DB_PASSWORD=postgres
ENV KC_PROXY_HEADERS=xforwarded
ENV KC_HTTP_RELATIVE_PATH=/auth
ENV KC_HEALTH_ENABLED=true
ENV KC_HOSTNAME=keycloak-auth
#ENV KC_HOSTNAME_ADMIN=http://keycloak-auth:8080

COPY ecosystem.json opt/keycloak/data/import/ecosystem.json
#COPY ../keycloak-custom-event-listener/target/custom-event-listener.jar //opt/keycloak/providers/custom-event-listener.jar

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]
