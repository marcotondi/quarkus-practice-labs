package io.github.marcotondi.labs.configuration;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST endpoint. NON modificare.
 */
@Path("/api/settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsResource {

    @Inject
    AppSettings settings;

    @GET
    public Settings get() {
        return new Settings(settings.mode(), settings.team());
    }
}