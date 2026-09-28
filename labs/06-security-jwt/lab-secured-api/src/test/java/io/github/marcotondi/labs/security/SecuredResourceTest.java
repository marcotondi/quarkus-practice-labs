package io.github.marcotondi.labs.security;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) i casi su /api/user, /api/admin e /api/me
 * falliscono: manca il controllo di accesso.
 */
@QuarkusTest
class SecuredResourceTest {

    private static final String ISSUER = "https://labs.marcotondi.github.io";

    /** Firma un token reale con la chiave privata di test. */
    private String token(String username, String... roles) {
        return Jwt.claims()
                .issuer(ISSUER)
                .upn(username)
                .claim("preferred_username", username)
                .groups(Set.of(roles))
                .expiresIn(Duration.ofMinutes(5))
                .sign();
    }

    @Test
    void publicEndpointIsOpen() {
        given()
            .when().get("/api/public")
            .then()
                .statusCode(200)
                .body(equalTo("public"));
    }

    @Test
    void userEndpointRejectsAnonymous() {
        given()
            .when().get("/api/user")
            .then()
                .statusCode(401);
    }

    @Test
    void userEndpointAllowsUserRole() {
        given()
            .auth().oauth2(token("alice", "user"))
            .when().get("/api/user")
            .then()
                .statusCode(200)
                .body(equalTo("user"));
    }

    @Test
    void adminEndpointRejectsUserRole() {
        given()
            .auth().oauth2(token("alice", "user"))
            .when().get("/api/admin")
            .then()
                .statusCode(403);
    }

    @Test
    void adminEndpointAllowsAdminRole() {
        given()
            .auth().oauth2(token("root", "admin"))
            .when().get("/api/admin")
            .then()
                .statusCode(200)
                .body(equalTo("admin"));
    }

    @Test
    void meReturnsUsernameAndRoles() {
        given()
            .auth().oauth2(token("alice", "user"))
            .when().get("/api/me")
            .then()
                .statusCode(200)
                .body("username", equalTo("alice"))
                .body("roles", hasItem("user"));
    }
}