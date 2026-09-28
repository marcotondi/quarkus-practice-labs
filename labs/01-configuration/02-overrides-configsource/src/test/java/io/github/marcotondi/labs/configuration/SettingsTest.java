package io.github.marcotondi.labs.configuration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Casi di test dell'esaminatore (profilo di default).
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
class SettingsTest {

    @Test
    void defaultModeComesFromConfigPropertyDefault() {
        given()
            .when().get("/api/settings")
            .then()
                .statusCode(200)
                .body("mode", equalTo("standard"));
    }

    @Test
    void customConfigSourceWinsByOrdinal() {
        given()
            .when().get("/api/settings")
            .then()
                .statusCode(200)
                .body("team", equalTo("platform"));
    }
}