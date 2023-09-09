package com.bookingusers.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeycloakAdminClient {

    private static final String realm = "booking-realm";

    @Value("${keycloak.baseUrl}")
    private String serverUrl;

    @Value("${spring.security.oauth2.client.registration.keycloak.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.clientSecret}")
    private String clientSecret;


    public String createUser(String username, String password) {

        Keycloak keycloak = getKeycloakClientCredentials();

        //password
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCredentials(List.of(passwordCred));

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Create user
        Response response = usersResource.create(user);

        String userId = CreatedResponseUtil.getCreatedId(response);
        System.out.println("User created on keycloak with id: " + userId);

        return userId;
    }

    public String login(String username, String password) {

        Keycloak keycloak = getKeycloakPasswordCredentials(username, password);
        AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();

        return accessTokenResponse.getToken();
    }

    private Keycloak getKeycloakClientCredentials() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    private Keycloak getKeycloakPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .build();
    }
}
