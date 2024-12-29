package org.example.redis_sink.serialization.element;

import org.example.redis_sink.serialization.RedisType;

import java.io.Serializable;

public abstract class RedisElement implements Serializable {
    private final RedisType type;

    public RedisElement(RedisType type) {
        this.type = type;
    }

    public abstract String getKey();
    public abstract Object getValue();

    public RedisType getType() {
        return type;
    }
}
