package io.github.marcotondi.labs.restclient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: delega le chiamate al client REST.
 */
@Path("/api/catalog")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource {

    @Inject
    @RestClient
    WidgetClient client;

    @GET
    @Path("/widget/{id}")
    public Widget widget(@PathParam("id") Long id) {
        // TODO: Implementare qui -> return client.getWidget(id);
        return null;
    }

    @GET
    @Path("/header")
    public HeaderEcho header() {
        // TODO: Implementare qui -> return client.header();
        return null;
    }
}