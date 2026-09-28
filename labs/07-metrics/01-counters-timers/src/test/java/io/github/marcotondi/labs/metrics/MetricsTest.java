package io.github.marcotondi.labs.metrics;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono: nessuna metrica è definita.
 */
@QuarkusTest
class MetricsTest {

    @Test
    void countedMetricIsExposed() {
        given().when().get("/api/orders/place/SKU-1").then().statusCode(200);

        given()
            .when().get("/q/metrics")
            .then()
                .statusCode(200)
                .body(containsString("orders_placed_total"));
    }

    @Test
    void timedMetricIsExposed() {
        given().when().get("/api/orders/process/SKU-1").then().statusCode(200);

        given()
            .when().get("/q/metrics")
            .then()
                .statusCode(200)
                .body(containsString("orders_processing_seconds_count"));
    }

    @Test
    void customCounterIsExposed() {
        given().when().get("/api/orders/checkout/SKU-1").then().statusCode(200);

        given()
            .when().get("/q/metrics")
            .then()
                .statusCode(200)
                .body(containsString("orders_checkout_total"));
    }

    @Test
    void customTimerIsExposed() {
        given().when().get("/api/orders/lookup/SKU-1").then().statusCode(200);

        given()
            .when().get("/q/metrics")
            .then()
                .statusCode(200)
                .body(containsString("orders_lookup_seconds_count"));
    }
}