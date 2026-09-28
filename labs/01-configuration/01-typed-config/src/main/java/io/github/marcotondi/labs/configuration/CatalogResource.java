package io.github.marcotondi.labs.configuration;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Espone la configurazione risolta dal profilo attivo.
 * NON modificare: il task è nella configurazione, non qui.
 */
@Path("/api/catalog")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource {

    @Inject
    CatalogConfig config;

    @GET
    @Path("/info")
    public CatalogInfo info() {
        return new CatalogInfo(
                config.greeting(),
                config.itemsPerPage(),
                config.datasourceUrl(),
                config.newUiEnabled());
    }
}