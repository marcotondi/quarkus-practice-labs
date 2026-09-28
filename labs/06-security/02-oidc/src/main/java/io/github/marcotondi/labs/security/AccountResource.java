package io.github.marcotondi.labs.security;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio: proteggi gli endpoint con OIDC/RBAC.
 */
@Path("/api")
@Produces(MediaType.TEXT_PLAIN)
public class AccountResource {

    @Inject
    SecurityIdentity identity;

    @GET
    @Path("/public")
    public String publicEndpoint() {
        return "public";
    }

    // TODO: Implementare qui
    // - @Authenticated: solo utenti autenticati
    // - ritorna il nome dell'utente (identity.getPrincipal().getName())
    @GET
    @Path("/me")
    public String me() {
        return null;
    }

    // TODO: Implementare qui
    // - @RolesAllowed("admin"): solo ruolo admin
    @GET
    @Path("/admin")
    public String admin() {
        return "admin";
    }
}