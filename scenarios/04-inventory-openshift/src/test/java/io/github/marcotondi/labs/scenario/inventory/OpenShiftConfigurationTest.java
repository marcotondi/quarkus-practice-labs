package io.github.marcotondi.labs.scenario.inventory;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class OpenShiftConfigurationTest {

    private String property(String name) {
        return ConfigProvider.getConfig().getValue(name, String.class);
    }

    @Test
    void routeIsExposed() {
        assertEquals("true", property("quarkus.openshift.route.expose"));
    }

    @Test
    void probesPointToHealthEndpoints() {
        assertEquals("/q/health/live", property("quarkus.openshift.liveness-probe.http-path"));
        assertEquals("/q/health/ready", property("quarkus.openshift.readiness-probe.http-path"));
    }

    @Test
    void databaseConfigurationComesFromConfigMap() {
        assertEquals("inventory-config", property("quarkus.openshift.env.configmaps"));
    }
}
