package io.github.marcotondi.labs.ft;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
class PriceServiceTest {

    @Inject
    DownstreamClient client;

    @BeforeEach
    void resetClient() {
        client.reset();
    }

    @Test
    void retryRecoversFromTransientFailures() {
        client.failTimes(2);

        given()
            .when().get("/api/price/ABC")
            .then()
                .statusCode(200)
                .body("value", equalTo("price:ABC"));

        // 2 fallimenti + 1 successo = 3 chiamate al downstream
        assertEquals(3, client.calls());
    }

    @Test
    void fallbackWhenRetriesAreExhausted() {
        client.alwaysFail();

        given()
            .when().get("/api/price/ABC")
            .then()
                .statusCode(200)
                .body("value", equalTo("price-unavailable"));
    }

    @Test
    void timeoutReturnsFallback() {
        client.delay(500);

        given()
            .when().get("/api/quote/ABC")
            .then()
                .statusCode(200)
                .body("value", equalTo("quote-unavailable"));
    }

    @Test
    void circuitBreakerOpensAndFailsFast() {
        client.alwaysFail();

        // esaurisce la soglia di fallimenti (2 chiamate)
        given().when().get("/api/stock/ABC").then()
                .statusCode(200).body("value", equalTo("stock-unavailable"));
        given().when().get("/api/stock/ABC").then()
                .statusCode(200).body("value", equalTo("stock-unavailable"));

        int callsWhileClosed = client.calls();

        // circuito aperto: nessuna nuova chiamata al downstream
        given().when().get("/api/stock/ABC").then()
                .statusCode(200).body("value", equalTo("stock-unavailable"));

        assertEquals(callsWhileClosed, client.calls(),
                "con il circuito aperto il downstream non deve essere contattato");
    }
}