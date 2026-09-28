package io.github.marcotondi.labs.configuration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) il test fallisce: la configurazione non è
 * mappata.
 */
@QuarkusTest
class ServerConfigTest {

    @Test
    void hierarchicalConfigIsMapped() {
        given()
            .when().get("/api/server-config")
            .then()
                .statusCode(200)
                .body("host", equalTo("localhost"))
                .body("port", equalTo(9090))
                .body("maxConnections", equalTo(25))
                .body("timeoutSeconds", equalTo(30));
    }
}