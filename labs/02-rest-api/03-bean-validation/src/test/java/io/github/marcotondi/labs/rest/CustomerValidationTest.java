package io.github.marcotondi.labs.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) i payload non validi vengono accettati:
 * tutti i casi tranne quello valido falliscono.
 */
@QuarkusTest
class CustomerValidationTest {

    private ValidatableResponse post(String name, String email, int age, String loyaltyCode) {
        String body = "{\"name\":\"" + name + "\",\"email\":\"" + email
                + "\",\"age\":" + age + ",\"loyaltyCode\":\"" + loyaltyCode + "\"}";
        return given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().post("/api/customers")
            .then();
    }

    @Test
    void validCustomerIsAccepted() {
        post("Alice", "alice@example.com", 30, "ABC-123").statusCode(201);
    }

    @Test
    void blankNameIsRejected() {
        post("", "alice@example.com", 30, "ABC-123").statusCode(400);
    }

    @Test
    void tooShortNameIsRejected() {
        post("A", "alice@example.com", 30, "ABC-123").statusCode(400);
    }

    @Test
    void invalidEmailIsRejected() {
        post("Alice", "not-an-email", 30, "ABC-123").statusCode(400);
    }

    @Test
    void underageIsRejected() {
        post("Alice", "alice@example.com", 15, "ABC-123").statusCode(400);
    }

    @Test
    void invalidLoyaltyCodeIsRejected() {
        post("Alice", "alice@example.com", 30, "abc-12").statusCode(400);
    }
}