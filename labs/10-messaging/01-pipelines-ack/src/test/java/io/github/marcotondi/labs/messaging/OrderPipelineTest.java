package io.github.marcotondi.labs.messaging;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) falliscono: il messaggio non viene elaborato.
 */
@QuarkusTest
class OrderPipelineTest {

    /** Attende (fino a ~5s) che il messaggio compaia tra quelli elaborati. */
    private String awaitProcessed(String expected) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            String body = given()
                .when().get("/api/orders")
                .then()
                    .statusCode(200)
                .extract().asString();
            if (body.contains(expected)) {
                return body;
            }
            Thread.sleep(100);
        }
        return given().when().get("/api/orders").then().extract().asString();
    }

    @Test
    void messageFlowsThroughPipeline() throws InterruptedException {
        given()
            .when().post("/api/orders/ORD-1")
            .then()
                .statusCode(202);

        String processed = awaitProcessed("processed:ORD-1");
        assertTrue(processed.contains("processed:ORD-1"),
                "il messaggio non ha attraversato la pipeline: " + processed);
    }
}