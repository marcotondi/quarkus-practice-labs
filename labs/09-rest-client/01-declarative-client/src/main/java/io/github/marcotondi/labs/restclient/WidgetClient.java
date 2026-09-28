package io.github.marcotondi.labs.restclient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: aggiungi l'header custom al client REST.
 *
 * configKey "widgets-api" corrisponde alla proprietà
 * quarkus.rest-client.widgets-api.url.
 */
@RegisterRestClient(configKey = "widgets-api")
@Path("/api/widgets")
@Produces(MediaType.APPLICATION_JSON)
public interface WidgetClient {

    // TODO: Implementare qui
    // Aggiungi @ClientHeaderParam(name = "X-Client", value = "quarkus-labs")
    @GET
    @Path("/{id}")
    Widget getWidget(@PathParam("id") Long id);

    // TODO: Implementare qui
    // Aggiungi @ClientHeaderParam(name = "X-Client", value = "quarkus-labs")
    @GET
    @Path("/header")
    HeaderEcho header();
}