package io.github.marcotondi.labs.scenario.inventory;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class HealthEndpointsTest {

    @Test
    void livenessIsUp() {
        given()
                .when().get("/q/health/live")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.name", hasItem("inventory-memory"))
                .body("checks.find { it.name == 'inventory-memory' }.data.freeMemoryBytes", notNullValue());
    }

    @Test
    void readinessIsUpWithDatabaseAvailable() {
        given()
                .when().get("/q/health/ready")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.name", hasItem("inventory-database"))
                .body("checks.find { it.name == 'inventory-database' }.data.database", equalTo("reachable"));
    }

    @Test
    void deepChecksGroupExposesBothChecks() {
        given()
                .when().get("/q/health/group/deep-checks")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.name", hasItem("inventory-memory"))
                .body("checks.name", hasItem("inventory-database"));
    }
}
