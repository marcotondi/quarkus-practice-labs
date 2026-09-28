package io.github.marcotondi.labs.openapi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * Casi di test dell'esaminatore.
 * Oggi (codice di partenza) TUTTI falliscono: l'API non è documentata.
 */
@QuarkusTest
class OpenApiTest {

    private ValidatableResponse openApi() {
        return given()
            .accept("application/json")
            .when().get("/q/openapi")
            .then()
                .statusCode(200);
    }

    @Test
    void operationSummaryIsDocumented() {
        openApi().body(containsString("Elenca i widget"));
    }

    @Test
    void operationIdIsDocumented() {
        openApi().body(containsString("listWidgets"));
    }

    @Test
    void responseDescriptionIsDocumented() {
        openApi().body(containsString("Widget creato"));
    }

    @Test
    void schemaFieldDescriptionIsDocumented() {
        openApi().body(containsString("Nome visualizzato del widget"));
    }

    @Test
    void tagIsDocumented() {
        openApi().body(containsString("Gestione widget"));
    }
}