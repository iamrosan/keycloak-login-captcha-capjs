package com.capjs.keycloak.captcha;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Content;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CapJsAuthenticator implements Authenticator {

    private static final String CAPTCHA_SECRET = "your-secret-key";
    private static final String CAPTCHA_VERIFY_URL = "https://your-captcha-server.com/verify";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String captchaToken = context.getHttpRequest().getDecodedFormParameters().getFirst("cap-token");

        if (captchaToken == null || captchaToken.isEmpty()) {
            Response challenge = context.form()
                .setError("CAPTCHA token is missing.")
                .createForm("login.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
            return;
        }

        boolean isValid = verifyCaptchaToken(captchaToken);

        if (isValid) {
            context.success();
        } else {
            Response challenge = context.form()
                .setError("Invalid CAPTCHA.")
                .createForm("login.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
        }
    }

    private boolean verifyCaptchaToken(String token) {
        try {
            Content content = Request.Post(CAPTCHA_VERIFY_URL)
                .bodyForm(Form.form()
                    .add("secret", CAPTCHA_SECRET)
                    .add("response", token)
                    .build())
                .execute()
                .returnContent();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> response = mapper.readValue(content.asString(), Map.class);
            return Boolean.TRUE.equals(response.get("success"));
        } catch (IOException e) {
            // Log the exception
            return false;
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // No action required
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(org.keycloak.models.KeycloakSession session, 
                               org.keycloak.models.RealmModel realm, 
                               UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(org.keycloak.models.KeycloakSession session, 
                                  org.keycloak.models.RealmModel realm, 
                                  UserModel user) {
        // No required actions
    }

    @Override
    public void close() {
        // No resources to close
    }
}