package io.github.marcotondi.labs.configuration;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: inietta la configurazione con @ConfigProperty.
 */
@ApplicationScoped
public class AppSettings {

    // TODO: Implementare qui
    // Inietta "app.mode" con @ConfigProperty e defaultValue "standard".
    String mode;

    // TODO: Implementare qui
    // Inietta "app.team" con @ConfigProperty (la proprietà esiste già).
    String team;

    public String mode() {
        return mode;
    }

    public String team() {
        return team;
    }
}