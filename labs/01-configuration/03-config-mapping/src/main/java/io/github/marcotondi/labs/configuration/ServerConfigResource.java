package io.github.marcotondi.labs.configuration;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * ATTENZIONE: questo file è INCOMPLETO. I TODO sono il task del
 * laboratorio.
 */
@Path("/api/server-config")
@Produces(MediaType.APPLICATION_JSON)
public class ServerConfigResource {

    // TODO: Implementare qui
    // 1. Crea l'interfaccia ServerConfig annotata
    //    @ConfigMapping(prefix = "server") con:
    //      String host();
    //      int port();
    //      Limits limits();
    //      interface Limits { int maxConnections(); @WithDefault("30") int timeoutSeconds(); }
    // 2. Iniettala qui.
    // 3. Implementa info() mappando i valori su ServerInfo.

    @GET
    public ServerInfo info() {
        return null;
    }
}