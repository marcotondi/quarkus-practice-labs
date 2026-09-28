package io.github.marcotondi.labs.restclient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio.
 */
@Path("/api/catalog")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource {

    @Inject
    @RestClient
    WidgetClient client;

    // TODO: Implementare qui
    // 1. Chiama client.getWidget(id) e rispondi 200 col widget.
    // 2. Se viene lanciata WidgetNotFoundException, rispondi 404 con
    //    new ErrorResponse("not_found", e.getMessage()).
    @GET
    @Path("/widget/{id}")
    public Response widget(@PathParam("id") Long id) {
        return null;
    }
}