package io.github.marcotondi.labs.scenario.inventory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/stock")
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {

    @GET
    public List<StockItem> list() {
        return StockItem.listAll();
    }
}
