package com.redhat.ex378.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono. Supererai il lab
 * quando passeranno tutti.
 */
@QuarkusTest
public class BookResourceTest {

    @Test
    void testListBooks() {
        given()
            .when().get("/api/books")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThanOrEqualTo(2))
                .body("title", hasItems("1984", "Brave New World"));
    }

    @Test
    void testGetExistingBook() {
        given()
            .when().get("/api/books/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("1984"))
                .body("author", equalTo("George Orwell"))
                .body("year", equalTo(1949));
    }

    @Test
    void testGetMissingBookReturns404() {
        given()
            .when().get("/api/books/999")
            .then()
                .statusCode(404);
    }

    @Test
    void testCreateBookReturns201WithLocation() {
        io.restassured.response.Response response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "Fahrenheit 451", "author": "Ray Bradbury", "year": 1953}
                """)
            .when().post("/api/books")
            .thenReturn();

        String createdId = response.path("id").toString();
        String location = response.getHeader("Location");

        response.then()
            .statusCode(Status.CREATED.getStatusCode())
            .body("id", notNullValue())
            .body("title", equalTo("Fahrenheit 451"));

        // Location puo' essere assoluta o relativa (RESTEasy Reactive
        // risolve gli URI relativi contro l'URL della richiesta):
        // deve semplicemente puntare alla risorsa appena creata.
        assertTrue(location != null && location.endsWith("/api/books/" + createdId),
            "Location inattesa: " + location);

        // il libro creato deve essere realmente raggiungibile
        given()
            .when().get("/api/books/" + createdId)
            .then()
                .statusCode(200)
                .body("title", equalTo("Fahrenheit 451"));
    }

    @Test
    void testCreateBookRejectsBlankTitle() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "", "author": "Anonimo", "year": 1950}
                """)
            .when().post("/api/books")
            .then()
                .statusCode(400);
    }

    @Test
    void testCreateBookRejectsMissingYear() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "Dune", "author": "Frank Herbert"}
                """)
            .when().post("/api/books")
            .then()
                .statusCode(400);
    }
}