package com.capjs.keycloak.captcha;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.Config.Scope;

public class CapJsAuthenticatorFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "capjs-authenticator";

    @Override
    public Authenticator create(KeycloakSession session) {
        return new CapJsAuthenticator();
    }

    @Override
    public void init(Scope config) {
        // Initialization if needed
    }

    @Override
    public void postInit(org.keycloak.models.KeycloakSessionFactory factory) {
        // Post-initialization if needed
    }

    @Override
    public void close() {
        // Cleanup if needed
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "CAP JS CAPTCHA Authenticator";
    }

    @Override
    public String getHelpText() {
        return "Validates CAPTCHA token using CAP JS.";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public org.keycloak.provider.ProviderConfigProperty getConfigProperties() {
        return null;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }
}
