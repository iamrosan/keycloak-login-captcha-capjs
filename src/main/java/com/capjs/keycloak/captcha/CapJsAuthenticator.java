package com.capjs.keycloak.captcha;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.forms.login.LoginFormsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

public class CapJsUsernamePasswordForm extends UsernamePasswordForm {
    public static final String PROVIDER_ID = "capjs-username-password-form";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (context.getHttpRequest().getFormParameters().containsKey("capjs_token")) {
            String token = context.getHttpRequest().getFormParameters().getFirst("capjs_token");
            if (validateCapJsToken(token)) {
                super.authenticate(context);
            } else {
                context.getEvent().error("Invalid Cap.js token");
                Response challenge = context.form()
                        .setError("Invalid or expired CAPTCHA token.")
                        .createForm("login.ftl");
                context.challenge(challenge);
            }
        } else {
            Response challenge = context.form().createForm("login.ftl");
            context.challenge(challenge);
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String token = formData.getFirst("capjs_token");
        if (validateCapJsToken(token)) {
            super.action(context);
        } else {
            context.getEvent().error("Invalid Cap.js token");
            Response challenge = context.form()
                    .setError("Invalid or expired CAPTCHA token.")
                    .createForm("login.ftl");
            context.challenge(challenge);
        }
    }

    private boolean validateCapJsToken(String token) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://kc-test.roshanshrestha99.com.np/api/captcha/validate");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity("{\"token\":\"" + token + "\"}"));
            try (CloseableHttpResponse response = client.execute(post)) {
                String result = EntityUtils.toString(response.getEntity());
                return response.getStatusLine().getStatusCode() == 200 && result.contains("\"success\":true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}