package io.github.marcotondi.labs.restclient;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono.
 */
@QuarkusTest
@QuarkusTestResource(DownstreamTestServer.class)
class CatalogResourceTest {

    @Test
    void widgetComesFromDownstream() {
        given()
            .when().get("/api/catalog/widget/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Gadget"));
    }

    @Test
    void clientHeaderIsSent() {
        given()
            .when().get("/api/catalog/header")
            .then()
                .statusCode(200)
                .body("echo", equalTo("quarkus-labs"));
    }
}