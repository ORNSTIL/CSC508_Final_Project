package org.example;

/**
 * Enum for managing server configurations.
 *
 * @version 1.0
 * @author Luca Ornstil
 * @author Monish Suresh
 * @author Christine Widden
 */

public enum ServerConfig {
    URSIM_SERVER("localhost", 30002);

    private final String host;
    private final int port;

    ServerConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}