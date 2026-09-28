package io.github.marcotondi.labs.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
class ItemResourceTest {

    private long createItem(String name, String price, int quantity) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + name + "\",\"price\":" + price + ",\"quantity\":" + quantity + "}")
            .when().post("/api/items")
            .then()
                .statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    @Test
    void listContainsSeededItems() {
        given()
            .when().get("/api/items")
            .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(3))
                .body("name", hasItems("Mechanical Keyboard", "Wireless Mouse", "USB-C Hub"));
    }

    @Test
    void getSeededItemById() {
        given()
            .when().get("/api/items/1")
            .then()
                .statusCode(200)
                .body("name", equalTo("Mechanical Keyboard"))
                .body("quantity", equalTo(12));
    }

    @Test
    void getMissingItemReturns404() {
        given()
            .when().get("/api/items/9999")
            .then()
                .statusCode(404);
    }

    @Test
    void createPersistsItem() {
        long id = createItem("Monitor 27", "199.00", 5);

        given()
            .when().get("/api/items/" + id)
            .then()
                .statusCode(200)
                .body("name", equalTo("Monitor 27"))
                .body("quantity", equalTo(5));
    }

    @Test
    void updatePersistsChanges() {
        long id = createItem("Tmp", "1.00", 1);

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Tmp Updated\",\"price\":2.00,\"quantity\":9}")
            .when().put("/api/items/" + id)
            .then()
                .statusCode(200);

        given()
            .when().get("/api/items/" + id)
            .then()
                .statusCode(200)
                .body("name", equalTo("Tmp Updated"))
                .body("quantity", equalTo(9));
    }

    @Test
    void deleteRemovesItem() {
        long id = createItem("To Delete", "3.00", 3);

        given().when().delete("/api/items/" + id).then().statusCode(204);
        given().when().get("/api/items/" + id).then().statusCode(404);
    }

    @Test
    void searchByNameReturnsMatches() {
        given()
            .when().get("/api/items/search?name=Mechanical Keyboard")
            .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("name", hasItems("Mechanical Keyboard"));
    }

    @Test
    void inStockExcludesUnavailableItems() {
        given()
            .when().get("/api/items/in-stock")
            .then()
                .statusCode(200)
                .body("name", hasItems("Mechanical Keyboard", "USB-C Hub"))
                .body("name", not(hasItems("Wireless Mouse")));
    }
}