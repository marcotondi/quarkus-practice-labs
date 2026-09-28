package io.github.marcotondi.labs.openapi;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: documenta l'API con le annotazioni MicroProfile OpenAPI.
 *
 * TODO: aggiungi @Tag(name = "Widgets", description = "Gestione widget")
 *       sulla classe (org.eclipse.microprofile.openapi.annotations.tags.Tag).
 */
@Path("/api/widgets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WidgetResource {

    // TODO: Implementare qui
    // @Operation(summary = "Elenca i widget", operationId = "listWidgets")
    @GET
    public List<Widget> list() {
        return List.of(new Widget(1L, "Gadget"));
    }

    // TODO: Implementare qui
    // @Operation(summary = "Crea un widget")
    // @APIResponse(responseCode = "201", description = "Widget creato")
    @POST
    public Response create(Widget widget) {
        widget.id = 2L;
        return Response.status(Response.Status.CREATED).entity(widget).build();
    }
}