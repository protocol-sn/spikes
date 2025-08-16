FROM quay.io/keycloak/keycloak:25.0

ENV KC_METRICS_ENABLED=true
ENV KC_HOSTNAME_STRICT=false
ENV KC_HTTP_ENABLED=true
ENV PROXY_ADDRESS_FORWARDING=true
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin
ENV KC_PROXY_HEADERS=xforwarded
ENV KC_HTTP_RELATIVE_PATH=/auth
ENV KC_HEALTH_ENABLED=true
ENV KC_HOSTNAME=keycloak-auth

COPY ecosystem.json opt/keycloak/data/import/ecosystem.json

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]
