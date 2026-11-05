package io.github.marcotondi.labs.scenario.payment;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService service;

    @POST
    public Response create(OrderRequest request) {
        return Response.status(Response.Status.CREATED).entity(service.place(request)).build();
    }

    @GET
    @Path("/{id}")
    public OrderResponse get(@PathParam("id") Long id) {
        return service.get(id);
    }
}
