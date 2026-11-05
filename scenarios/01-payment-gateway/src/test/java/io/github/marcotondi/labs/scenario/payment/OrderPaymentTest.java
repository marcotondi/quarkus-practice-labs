package io.github.marcotondi.labs.scenario.payment;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.smallrye.faulttolerance.api.CircuitBreakerMaintenance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(PaymentGatewayTestServer.class)
class OrderPaymentTest {

    @Inject
    CircuitBreakerMaintenance circuitBreakerMaintenance;

    @BeforeEach
    void resetDownstreamAndCircuitBreaker() {
        PaymentGatewayTestServer.instance().reset();
        circuitBreakerMaintenance.resetAll();
    }

    private Response postOrder(String body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/orders");
    }

    @Test
    void paidOrderIsPersistedWithPaidStatus() {
        Response response = postOrder("{\"amount\":125.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", equalTo("PAID"))
                .body("currency", equalTo("EUR"))
                .extract().response();

        String id = response.path("id").toString();

        given()
                .when().get("/orders/" + id)
                .then()
                .statusCode(200)
                .body("status", equalTo("PAID"));

        assertEquals(1, PaymentGatewayTestServer.instance().calls());
    }

    @Test
    void invalidAmountIsRejectedWithoutCallingGateway() {
        postOrder("{\"amount\":-1.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(400);

        assertEquals(0, PaymentGatewayTestServer.instance().calls());
    }

    @Test
    void invalidCurrencyIsRejectedWithoutCallingGateway() {
        postOrder("{\"amount\":10.00,\"currency\":\"eur\"}")
                .then()
                .statusCode(400);

        assertEquals(0, PaymentGatewayTestServer.instance().calls());
    }

    @Test
    void retriesTransientFailuresBeforeGivingUp() {
        PaymentGatewayTestServer.instance().failTimes(3);

        postOrder("{\"amount\":50.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(201)
                .body("status", equalTo("PAID"));

        assertEquals(4, PaymentGatewayTestServer.instance().calls());
    }

    @Test
    void failedPaymentMarksOrderAndReturns503() {
        PaymentGatewayTestServer.instance().alwaysFail();

        Response response = postOrder("{\"amount\":75.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(503)
                .body("error", equalTo("PAYMENT_UNAVAILABLE"))
                .body("orderId", notNullValue())
                .extract().response();

        String id = response.path("orderId").toString();

        given()
                .when().get("/orders/" + id)
                .then()
                .statusCode(200)
                .body("status", equalTo("FAILED_PAYMENT"));
    }

    @Test
    void slowGatewayIsInterruptedByTimeout() {
        PaymentGatewayTestServer.instance().delay(1200);

        postOrder("{\"amount\":30.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(503)
                .body("error", equalTo("PAYMENT_UNAVAILABLE"));
    }

    @Test
    void readinessFailsWhenCircuitBreakerIsOpen() {
        given()
                .when().get("/q/health/ready")
                .then()
                .statusCode(200)
                .body("checks.name", hasItem("payment-gateway"));

        PaymentGatewayTestServer.instance().alwaysFail();
        for (int i = 0; i < 4; i++) {
            postOrder("{\"amount\":10.00,\"currency\":\"EUR\"}")
                    .then()
                    .statusCode(503);
        }

        given()
                .when().get("/q/health/ready")
                .then()
                .statusCode(503)
                .body("status", equalTo("DOWN"));
    }

    @Test
    void openCircuitBreakerStopsCallingTheGateway() {
        PaymentGatewayTestServer.instance().alwaysFail();
        for (int i = 0; i < 4; i++) {
            postOrder("{\"amount\":20.00,\"currency\":\"EUR\"}")
                    .then()
                    .statusCode(503);
        }

        int callsBeforeOpenCircuit = PaymentGatewayTestServer.instance().calls();

        postOrder("{\"amount\":20.00,\"currency\":\"EUR\"}")
                .then()
                .statusCode(503);

        assertEquals(callsBeforeOpenCircuit, PaymentGatewayTestServer.instance().calls(),
                "con il circuito aperto il gateway non deve essere contattato");
        assertTrue(callsBeforeOpenCircuit > 0);
    }

    @Test
    void missingOrderReturns404() {
        given()
                .when().get("/orders/9999")
                .then()
                .statusCode(404);
    }
}
