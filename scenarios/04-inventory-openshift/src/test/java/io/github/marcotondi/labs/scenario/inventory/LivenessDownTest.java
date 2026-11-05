package io.github.marcotondi.labs.scenario.inventory;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestProfile(LivenessDownTest.LowMemoryProfile.class)
class LivenessDownTest {

    public static class LowMemoryProfile implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("inventory.health.min-free-memory-bytes", "9223372036854775807");
        }
    }

    @Test
    void livenessIsDownWhenFreeMemoryIsBelowThreshold() {
        given()
                .when().get("/q/health/live")
                .then()
                .statusCode(503)
                .body("status", equalTo("DOWN"));
    }
}
