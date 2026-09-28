package io.github.marcotondi.labs.messaging;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;

/**
 * REST endpoint. NON modificare.
 */
@Path("/api/orders")
public class OrderResource {

    @Inject
    @Channel("orders-in")
    Emitter<String> emitter;

    @Inject
    OrderProcessor processor;

    @POST
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response submit(@PathParam("id") String id) {
        emitter.send(id);
        return Response.accepted().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> processed() {
        return processor.processed();
    }
}