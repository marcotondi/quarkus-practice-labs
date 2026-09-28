package io.github.marcotondi.labs.metrics;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST endpoint. NON modificare: chiama il servizio e basta.
 */
@Path("/api/orders")
public class OrderResource {

    @Inject
    OrderMetricsService service;

    @GET
    @Path("/place/{sku}")
    @Produces(MediaType.TEXT_PLAIN)
    public String place(@PathParam("sku") String sku) {
        return service.placeOrder(sku);
    }

    @GET
    @Path("/process/{sku}")
    @Produces(MediaType.TEXT_PLAIN)
    public String process(@PathParam("sku") String sku) {
        return service.processOrder(sku);
    }

    @GET
    @Path("/checkout/{sku}")
    @Produces(MediaType.TEXT_PLAIN)
    public String checkout(@PathParam("sku") String sku) {
        return service.checkout(sku);
    }

    @GET
    @Path("/lookup/{sku}")
    @Produces(MediaType.TEXT_PLAIN)
    public String lookup(@PathParam("sku") String sku) {
        return service.lookup(sku);
    }
}