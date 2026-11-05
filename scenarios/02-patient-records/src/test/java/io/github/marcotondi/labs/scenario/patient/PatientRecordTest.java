package io.github.marcotondi.labs.scenario.patient;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class PatientRecordTest {

    private static final String ISSUER = "https://labs.marcotondi.github.io";

    private String token(String username, String... roles) {
        return Jwt.claims()
                .issuer(ISSUER)
                .upn(username)
                .claim("preferred_username", username)
                .groups(Set.of(roles))
                .expiresIn(Duration.ofMinutes(5))
                .sign();
    }

    private Long seedPatient(String fiscalCode) {
        Patient patient = new Patient("Mario", "Rossi", fiscalCode, LocalDate.of(1980, 1, 1));
        QuarkusTransaction.requiringNew().run(patient::persist);
        return patient.id;
    }

    private String validBody(String fiscalCode) {
        return "{\"firstName\":\"Giulia\",\"lastName\":\"Bianchi\","
                + "\"fiscalCode\":\"" + fiscalCode + "\",\"birthDate\":\"1990-05-17\"}";
    }

    @Test
    void anonymousRequestIsRejected() {
        Long id = seedPatient("RSSMRA80A01H501U");

        given()
                .when().get("/patients/" + id)
                .then()
                .statusCode(401);
    }

    @Test
    void doctorCanReadPatient() {
        Long id = seedPatient("RSSMRA80A01H501U");

        given()
                .auth().oauth2(token("alice", "doctor"))
                .when().get("/patients/" + id)
                .then()
                .statusCode(200)
                .body("fiscalCode", equalTo("RSSMRA80A01H501U"));
    }

    @Test
    void doctorCannotDeletePatient() {
        Long id = seedPatient("RSSMRA80A01H501U");

        given()
                .auth().oauth2(token("alice", "doctor"))
                .when().delete("/patients/" + id)
                .then()
                .statusCode(403);

        given()
                .auth().oauth2(token("alice", "doctor"))
                .when().get("/patients/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    void adminCanDeletePatient() {
        Long id = seedPatient("RSSMRA80A01H501U");

        given()
                .auth().oauth2(token("root", "admin"))
                .when().delete("/patients/" + id)
                .then()
                .statusCode(204);

        given()
                .auth().oauth2(token("root", "admin"))
                .when().get("/patients/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void invalidFiscalCodeIsRejected() {
        given()
                .auth().oauth2(token("root", "admin"))
                .contentType(ContentType.JSON)
                .body(validBody("NONVALIDO"))
                .when().post("/patients")
                .then()
                .statusCode(400);
    }

    @Test
    void missingPatientReturns404() {
        given()
                .auth().oauth2(token("alice", "doctor"))
                .when().get("/patients/9999")
                .then()
                .statusCode(404);
    }

    @Test
    void deniedAccessIsCountedPerRole() {
        Long id = seedPatient("RSSMRA80A01H501U");

        given()
                .auth().oauth2(token("nurse-1", "nurse"))
                .when().get("/patients/" + id)
                .then()
                .statusCode(403);

        given()
                .when().get("/q/metrics")
                .then()
                .statusCode(200)
                .body(containsString("patients_access_denied_total{role=\"nurse\"}"));

        given()
                .when().get("/patients/" + id)
                .then()
                .statusCode(401);

        given()
                .when().get("/q/metrics")
                .then()
                .statusCode(200)
                .body(not(containsString("role=\"anonymous\"")));
    }

    @Test
    void auditUserComesFromTheToken() {
        Long id = seedPatient("RSSMRA80A01H501U");

        Response alice = given()
                .auth().oauth2(token("alice", "doctor"))
                .when().get("/patients/" + id)
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals("alice", alice.path("auditUser"));

        Response bob = given()
                .auth().oauth2(token("bob", "doctor"))
                .when().get("/patients/" + id)
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals("bob", bob.path("auditUser"));
    }

    @Test
    void doctorCanCreatePatient() {
        Response response = given()
                .auth().oauth2(token("alice", "doctor"))
                .contentType(ContentType.JSON)
                .body(validBody("BNCGLI90E57F205X"))
                .when().post("/patients")
                .then()
                .statusCode(201)
                .body("id", org.hamcrest.Matchers.notNullValue())
                .body("fiscalCode", equalTo("BNCGLI90E57F205X"))
                .extract().response();

        given()
                .auth().oauth2(token("alice", "doctor"))
                .when().get("/patients/" + response.path("id").toString())
                .then()
                .statusCode(200);
    }
}
