package io.github.marcotondi.labs.configuration;

/**
 * DTO di risposta. NON modificare.
 */
public class ServerInfo {

    public String host;
    public int port;
    public int maxConnections;
    public int timeoutSeconds;

    public ServerInfo() {
    }

    public ServerInfo(String host, int port, int maxConnections, int timeoutSeconds) {
        this.host = host;
        this.port = port;
        this.maxConnections = maxConnections;
        this.timeoutSeconds = timeoutSeconds;
    }
}