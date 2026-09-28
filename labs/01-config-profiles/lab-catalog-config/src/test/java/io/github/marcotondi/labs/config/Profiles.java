package io.github.marcotondi.labs.config;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * Profili usati dai test dell'esaminatore per forzare il profilo attivo.
 * NON modificare.
 */
public final class Profiles {

    private Profiles() {
    }

    public static class Dev implements QuarkusTestProfile {
        @Override
        public String getConfigProfile() {
            return "dev";
        }
    }

    public static class Prod implements QuarkusTestProfile {
        @Override
        public String getConfigProfile() {
            return "prod";
        }
    }
}