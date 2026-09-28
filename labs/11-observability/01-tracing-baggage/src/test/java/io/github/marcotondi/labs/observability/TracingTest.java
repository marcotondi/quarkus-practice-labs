package io.github.marcotondi.labs.observability;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) il test fallisce: nessuno span/baggage.
 */
@QuarkusTest
class TracingTest {

    @Test
    void baggageAndTraceIdArePropagated() {
        given()
            .when().get("/api/trace/ABC")
            .then()
                .statusCode(200)
                .body("requestId", equalTo("ABC"))
                .body("traceId", matchesPattern("[0-9a-f]{32}"))
                .body("traceId", not(equalTo("00000000000000000000000000000000")));
    }
}