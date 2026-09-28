package io.github.marcotondi.labs.ft;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST endpoint. NON modificare: chiama il servizio e basta.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class PriceResource {

    @Inject
    PriceService service;

    @GET
    @Path("/price/{sku}")
    public PriceResponse price(@PathParam("sku") String sku) {
        return new PriceResponse(service.getPrice(sku));
    }

    @GET
    @Path("/quote/{sku}")
    public PriceResponse quote(@PathParam("sku") String sku) {
        return new PriceResponse(service.getQuote(sku));
    }

    @GET
    @Path("/stock/{sku}")
    public PriceResponse stock(@PathParam("sku") String sku) {
        return new PriceResponse(service.getStock(sku));
    }
}