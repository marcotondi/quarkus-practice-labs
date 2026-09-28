package io.github.marcotondi.labs.configuration;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Verifica i valori del profilo "prod".
 */
@QuarkusTest
@TestProfile(Profiles.Prod.class)
class CatalogProdProfileTest {

    @Test
    void prodProfileValuesAreApplied() {
        given()
            .when().get("/api/catalog/info")
            .then()
                .statusCode(200)
                .body("greeting", equalTo("Hello"))
                .body("itemsPerPage", equalTo(50))
                .body("datasourceUrl", equalTo("jdbc:postgresql://prod-db:5432/catalog"))
                .body("newUiEnabled", equalTo(true));
    }
}