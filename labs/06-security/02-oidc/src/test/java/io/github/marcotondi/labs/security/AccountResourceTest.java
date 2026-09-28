package io.github.marcotondi.labs.security;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.OidcSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) i casi protetti falliscono.
 */
@QuarkusTest
class AccountResourceTest {

    @Test
    void publicEndpointIsOpen() {
        given()
            .when().get("/api/public")
            .then()
                .statusCode(200)
                .body(equalTo("public"));
    }

    @Test
    void meRejectsAnonymous() {
        given()
            .when().get("/api/me")
            .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "alice", roles = {"user"})
    @OidcSecurity
    void meReturnsPrincipalName() {
        given()
            .when().get("/api/me")
            .then()
                .statusCode(200)
                .body(equalTo("alice"));
    }

    @Test
    @TestSecurity(user = "alice", roles = {"user"})
    @OidcSecurity
    void adminRejectsUserRole() {
        given()
            .when().get("/api/admin")
            .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "root", roles = {"admin"})
    @OidcSecurity
    void adminAllowsAdminRole() {
        given()
            .when().get("/api/admin")
            .then()
                .statusCode(200)
                .body(equalTo("admin"));
    }
}