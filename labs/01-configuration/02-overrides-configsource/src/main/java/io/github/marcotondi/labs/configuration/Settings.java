package io.github.marcotondi.labs.configuration;

/**
 * DTO di risposta. NON modificare.
 */
public class Settings {

    public String mode;
    public String team;

    public Settings() {
    }

    public Settings(String mode, String team) {
        this.mode = mode;
        this.team = team;
    }
}