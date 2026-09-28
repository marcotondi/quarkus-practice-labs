package io.github.marcotondi.labs.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
class ReservationResourceTest {

    @Test
    void getExistingReturns200() {
        given()
            .when().get("/api/reservations/1")
            .then()
                .statusCode(200)
                .body("code", equalTo("R-100"));
    }

    @Test
    void getMissingReturns404WithErrorBody() {
        given()
            .when().get("/api/reservations/999")
            .then()
                .statusCode(404)
                .body("code", equalTo("not_found"))
                .body("message", notNullValue());
    }

    @Test
    void createValidReturns201WithLocation() {
        io.restassured.response.Response response = given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"R-200\",\"customer\":\"Bob\",\"seats\":3}")
            .when().post("/api/reservations")
            .thenReturn();

        response.then()
            .statusCode(201)
            .body("id", notNullValue());

        String id = response.path("id").toString();
        String location = response.getHeader("Location");
        assertTrue(location != null && location.endsWith("/api/reservations/" + id),
                "Location inattesa: " + location);
    }

    @Test
    void createDuplicateReturns409WithErrorBody() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"R-100\",\"customer\":\"Bob\",\"seats\":1}")
            .when().post("/api/reservations")
            .then()
                .statusCode(409)
                .body("code", equalTo("conflict"));
    }

    @Test
    void createInvalidReturns400() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"\",\"customer\":\"Bob\",\"seats\":0}")
            .when().post("/api/reservations")
            .then()
                .statusCode(400);
    }
}