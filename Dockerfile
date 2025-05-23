# unique-attribute-validator-provider
FROM maven:3.9.9-eclipse-temurin-21 AS maven

WORKDIR /app

COPY ./pom.xml .
COPY ./src .

RUN mvn clean package && \
    mkdir -p /result && \
    cp /app/target/keycloak-login-captcha-capjs-*.jar /result/keycloak-login-captcha-capjs.jar

# keycloak stage
FROM quay.io/keycloak/keycloak:26.0.5

WORKDIR /opt/keycloak 

COPY --from=maven /result/keycloak-login-captcha-capjs.jar /opt/keycloak/providers

# COPY themes /opt/keycloak/themes

RUN /opt/keycloak/bin/kc.sh build

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]