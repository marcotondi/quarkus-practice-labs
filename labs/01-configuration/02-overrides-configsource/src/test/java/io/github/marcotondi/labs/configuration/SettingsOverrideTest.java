package io.github.marcotondi.labs.configuration;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Verifica che un override di configurazione abbia la precedenza.
 */
@QuarkusTest
@TestProfile(ModeOverrideProfile.class)
class SettingsOverrideTest {

    @Test
    void configOverrideWins() {
        given()
            .when().get("/api/settings")
            .then()
                .statusCode(200)
                .body("mode", equalTo("overridden"));
    }
}