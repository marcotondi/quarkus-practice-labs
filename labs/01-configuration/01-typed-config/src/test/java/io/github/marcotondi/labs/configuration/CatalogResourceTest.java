package io.github.marcotondi.labs.configuration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Profilo attivo di default nei test: "test".
 * Oggi questo test fallisce: i valori non sono ancora differenziati
 * per profilo.
 */
@QuarkusTest
class CatalogResourceTest {

    @Test
    void testProfileValuesAreApplied() {
        given()
            .when().get("/api/catalog/info")
            .then()
                .statusCode(200)
                .body("greeting", equalTo("Hello from test"))
                .body("itemsPerPage", equalTo(5))
                .body("datasourceUrl", equalTo("jdbc:h2:mem:testdb"))
                .body("newUiEnabled", equalTo(false));
    }
}