package io.github.marcotondi.labs.observability;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST endpoint. NON modificare.
 */
@Path("/api/trace")
@Produces(MediaType.APPLICATION_JSON)
public class TraceResource {

    @Inject
    TraceService service;

    @GET
    @Path("/{requestId}")
    public TraceResult trace(@PathParam("requestId") String requestId) {
        return service.handle(requestId);
    }
}