# unique-attribute-validator-provider
FROM maven:3.9.9-eclipse-temurin-21 AS maven

WORKDIR /app

COPY core/pom.xml .
COPY core/src ./src

RUN mvn clean package && \
    mkdir -p /result && \
    cp /app/target/keycloak-capjs-spi-*.jar /result/keycloak-capjs-spi.jar

# keycloak stage
FROM quay.io/keycloak/keycloak:26.0.5

WORKDIR /opt/keycloak 

COPY --from=maven /result/keycloak-capjs-spi.jar /opt/keycloak/providers

# COPY themes /opt/keycloak/themes

RUN /opt/keycloak/bin/kc.sh build

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]