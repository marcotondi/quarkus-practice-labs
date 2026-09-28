package io.github.marcotondi.labs.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
class ProductResourceTest {

    private long createProduct(String name, String price, int stock) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + name + "\",\"price\":" + price + ",\"stock\":" + stock + "}")
            .when().post("/api/products")
            .then()
                .statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    @Test
    void listContainsSeededProducts() {
        given()
            .when().get("/api/products")
            .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(3))
                .body("name", hasItems("Mechanical Keyboard", "Wireless Mouse", "USB-C Hub"));
    }

    @Test
    void getSeededProductById() {
        given()
            .when().get("/api/products/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Mechanical Keyboard"))
                .body("stock", equalTo(12));
    }

    @Test
    void getMissingProductReturns404() {
        given()
            .when().get("/api/products/9999")
            .then()
                .statusCode(404);
    }

    @Test
    void createProductReturns201WithLocationAndPersists() {
        io.restassured.response.Response response = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Monitor 27\",\"price\":199.00,\"stock\":5}")
            .when().post("/api/products")
            .thenReturn();

        response.then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Monitor 27"))
            .body("stock", equalTo(5));

        String id = response.path("id").toString();
        String location = response.getHeader("Location");
        assertTrue(location != null && location.endsWith("/api/products/" + id),
                "Location inattesa: " + location);

        given()
            .when().get("/api/products/" + id)
            .then()
                .statusCode(200)
                .body("name", equalTo("Monitor 27"))
                .body("stock", equalTo(5));
    }

    @Test
    void updateProductPersistsChanges() {
        long id = createProduct("Tmp Product", "1.00", 1);

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Tmp Updated\",\"price\":2.00,\"stock\":9}")
            .when().put("/api/products/" + id)
            .then()
                .statusCode(200);

        given()
            .when().get("/api/products/" + id)
            .then()
                .statusCode(200)
                .body("name", equalTo("Tmp Updated"))
                .body("stock", equalTo(9));
    }

    @Test
    void updateMissingProductReturns404() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"x\",\"price\":1.00,\"stock\":1}")
            .when().put("/api/products/9999")
            .then()
                .statusCode(404);
    }

    @Test
    void deleteProductRemovesIt() {
        long id = createProduct("To Delete", "3.00", 3);

        given()
            .when().delete("/api/products/" + id)
            .then()
                .statusCode(204);

        given()
            .when().get("/api/products/" + id)
            .then()
                .statusCode(404);
    }

    @Test
    void deleteMissingProductReturns404() {
        given()
            .when().delete("/api/products/9999")
            .then()
                .statusCode(404);
    }

    @Test
    void searchByNameIsCaseInsensitive() {
        given()
            .when().get("/api/products/search?name=keyboard")
            .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("name", hasItems("Mechanical Keyboard"));
    }
}