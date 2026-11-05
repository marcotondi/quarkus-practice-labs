package io.github.marcotondi.labs.scenario.reactivepipeline;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ReactivePipelineTest {

    private Response submit(String body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/transactions");
    }

    private void pause() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Response awaitTransaction(String transactionId) {
        for (int i = 0; i < 50; i++) {
            Response response = given()
                    .when().get("/transactions/" + transactionId);
            if (response.statusCode() == 200) {
                return response;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return given().when().get("/transactions/" + transactionId);
    }

    private Response awaitAudit(String transactionId) {
        for (int i = 0; i < 50; i++) {
            Response response = given()
                    .when().get("/audit/" + transactionId);
            if (response.statusCode() == 200) {
                return response;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return given().when().get("/audit/" + transactionId);
    }

    @Test
    void validTransactionIsPersistedAndPublished() {
        submit("{\"transactionId\":\"TX-1\",\"amount\":100.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(202);

        awaitTransaction("TX-1")
                .then()
                .statusCode(200)
                .body("transactionId", equalTo("TX-1"))
                .body("status", equalTo("PROCESSED"));

        awaitAudit("TX-1")
                .then()
                .statusCode(200)
                .body("deliveries", equalTo(1))
                .body("persistedWhenReceived", equalTo(true));
    }

    @Test
    void invalidAmountIsNotPersistedNorPublished() {
        submit("{\"transactionId\":\"TX-INVALID\",\"amount\":-5.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(202);

        pause();

        given()
                .when().get("/audit/TX-INVALID")
                .then()
                .statusCode(404);

        given()
                .when().get("/transactions/TX-INVALID")
                .then()
                .statusCode(404);
    }

    @Test
    void duplicateTransactionIsNotStoredTwice() {
        submit("{\"transactionId\":\"TX-DUP\",\"amount\":10.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(202);
        awaitAudit("TX-DUP").then().statusCode(200).body("deliveries", equalTo(1));
        awaitTransaction("TX-DUP").then().statusCode(200);

        submit("{\"transactionId\":\"TX-DUP\",\"amount\":10.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(202);

        pause();

        awaitAudit("TX-DUP")
                .then()
                .statusCode(200)
                .body("deliveries", equalTo(1));
    }

    @Test
    void unknownTransactionReturns404() {
        given()
                .when().get("/transactions/UNKNOWN")
                .then()
                .statusCode(404);
    }
}
