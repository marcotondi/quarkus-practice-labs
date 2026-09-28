package io.github.marcotondi.labs.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
class AuthorResourceTest {

    private long createAuthor(String name, String... titles) {
        String books = Arrays.stream(titles)
                .map(t -> "\"" + t + "\"")
                .collect(Collectors.joining(","));
        String body = "{\"name\":\"" + name + "\",\"bookTitles\":[" + books + "]}";
        return given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().post("/api/authors")
            .then()
                .statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    @Test
    void createAndGetAuthorWithBooks() {
        long id = createAuthor("George Orwell", "1984", "Animal Farm");

        given()
            .when().get("/api/authors/" + id)
            .then()
                .statusCode(200)
                .body("name", equalTo("George Orwell"))
                .body("bookTitles", hasItems("1984", "Animal Farm"));
    }

    @Test
    void getMissingAuthorReturns404() {
        given()
            .when().get("/api/authors/9999")
            .then()
                .statusCode(404);
    }

    @Test
    void findByNameReturnsAuthor() {
        createAuthor("Aldous Huxley", "Brave New World");

        given()
            .when().get("/api/authors/by-name/Aldous Huxley")
            .then()
                .statusCode(200)
                .body("name", equalTo("Aldous Huxley"))
                .body("bookTitles", hasItems("Brave New World"));
    }

    @Test
    void listReturnsAuthorsWithBooks() {
        createAuthor("Ray Bradbury", "Fahrenheit 451");

        given()
            .when().get("/api/authors")
            .then()
                .statusCode(200)
                .body(containsString("Fahrenheit 451"));
    }
}