package io.github.marcotondi.labs.scenario.subscription;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class SubscriptionEngineTest {

    private Long seed(String plan, String price, SubscriptionStatus status, LocalDate expiresAt) {
        Subscription subscription = new Subscription(plan, new BigDecimal(price), status, expiresAt);
        QuarkusTransaction.requiringNew().run(subscription::persist);
        return subscription.id;
    }

    @Test
    void basicPlanPriceComesFromConfiguration() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"plan\":\"basic\"}")
                .when().post("/subscriptions")
                .then()
                .statusCode(201)
                .body("plan", equalTo("basic"))
                .body("status", equalTo("ACTIVE"))
                .body("monthlyPrice", equalTo(19.90f))
                .body("expiresAt", notNullValue());
    }

    @Test
    void premiumPlanPriceComesFromConfiguration() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"plan\":\"premium\"}")
                .when().post("/subscriptions")
                .then()
                .statusCode(201)
                .body("monthlyPrice", equalTo(49.90f));
    }

    @Test
    void unknownPlanIsRejected() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"plan\":\"platinum\"}")
                .when().post("/subscriptions")
                .then()
                .statusCode(400);
    }

    @Test
    void renewingActiveSubscriptionExtendsExpiryAndReprices() {
        LocalDate expiresAt = LocalDate.now().plusDays(10);
        Long id = seed("basic", "1.00", SubscriptionStatus.ACTIVE, expiresAt);

        Response response = given()
                .when().post("/subscriptions/" + id + "/renew")
                .then()
                .statusCode(200)
                .body("status", equalTo("ACTIVE"))
                .body("monthlyPrice", equalTo(19.90f))
                .extract().response();

        String renewedExpiry = response.path("expiresAt");
        assertTrue(LocalDate.parse(renewedExpiry).isAfter(expiresAt),
                "la scadenza non è stata estesa: " + renewedExpiry);
    }

    @Test
    void renewingExpiredSubscriptionReturnsConflict() {
        Long id = seed("basic", "19.90", SubscriptionStatus.EXPIRED, LocalDate.now().minusDays(5));

        given()
                .when().post("/subscriptions/" + id + "/renew")
                .then()
                .statusCode(409)
                .body("error", equalTo("ABBONAMENTO_SCADUTO"))
                .body("code", equalTo(409));

        given()
                .when().get("/subscriptions/" + id)
                .then()
                .statusCode(200)
                .body("status", equalTo("EXPIRED"));
    }

    @Test
    void missingSubscriptionReturns404() {
        given()
                .when().get("/subscriptions/9999")
                .then()
                .statusCode(404);

        given()
                .when().post("/subscriptions/9999/renew")
                .then()
                .statusCode(404);
    }

    @Test
    void activeSubscriptionIsReadable() {
        Long id = seed("premium", "49.90", SubscriptionStatus.ACTIVE, LocalDate.now().plusDays(3));

        given()
                .when().get("/subscriptions/" + id)
                .then()
                .statusCode(200)
                .body("plan", equalTo("premium"))
                .body("monthlyPrice", equalTo(49.90f))
                .body("expiresAt", notNullValue());
    }

    @Test
    void createdSubscriptionIsStoredWithOneMonthValidity() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"plan\":\"premium\"}")
                .when().post("/subscriptions")
                .then()
                .statusCode(201)
                .extract().response();

        LocalDate expiresAt = LocalDate.parse(response.path("expiresAt"));
        assertTrue(expiresAt.isAfter(LocalDate.now().plusDays(25)),
                "validità attesa di circa un mese: " + expiresAt);
        assertEquals(1, Subscription.count("id", Long.parseLong(response.path("id").toString())));
    }
}
