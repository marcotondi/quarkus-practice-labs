package io.github.marcotondi.labs.configuration;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

/**
 * Profilo di test che sovrascrive una proprietà di configurazione.
 * NON modificare.
 */
public class ModeOverrideProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("app.mode", "overridden");
    }
}