package io.github.marcotondi.labs.health;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono: non esiste ancora nessun
 * health check personalizzato.
 */
@QuarkusTest
class HealthCheckTest {

    @Inject
    DownstreamService downstream;

    @BeforeEach
    void resetState() {
        downstream.setUp(true);
    }

    @Test
    void livenessExposesAppCheck() {
        given()
            .when().get("/q/health/live")
            .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.name", hasItem("app"));
    }

    @Test
    void readinessIsUpWhenDownstreamIsUp() {
        given()
            .when().get("/q/health/ready")
            .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.name", hasItem("downstream"));
    }

    @Test
    void readinessIsDownWhenDownstreamIsDown() {
        downstream.setUp(false);

        given()
            .when().get("/q/health/ready")
            .then()
                .statusCode(503)
                .body("status", equalTo("DOWN"));
    }
}