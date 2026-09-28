package io.github.marcotondi.labs.security;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

/**
 * REST endpoint protetto con JWT (RBAC).
 *
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: aggiungi il controllo di accesso basato sui ruoli.
 */
@Path("/api")
public class SecuredResource {

    @GET
    @Path("/public")
    @Produces(MediaType.TEXT_PLAIN)
    public String publicEndpoint() {
        return "public";
    }

    // TODO: Implementare qui
    // Accessibile solo agli utenti autenticati con ruolo "user".
    @GET
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    public String userEndpoint() {
        return "user";
    }

    // TODO: Implementare qui
    // Accessibile solo agli utenti autenticati con ruolo "admin".
    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String adminEndpoint() {
        return "admin";
    }

    // TODO: Implementare qui
    // Richiede ruolo "user". Ritorna un JSON:
    //   {"username": <preferred_username del token>, "roles": [<gruppi>]}
    // Suggerimento: inietta org.eclipse.microprofile.jwt.JsonWebToken.
    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> me() {
        return null;
    }
}