package org.example.redis_sink.utils;

import java.io.Serializable;

public class RedisOptions implements Serializable {
    private String host;
    private int port;

    public RedisOptions(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
