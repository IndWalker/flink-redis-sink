package org.example.redis_sink.serialization.element;

import org.example.redis_sink.serialization.RedisType;

public class RedisStringElement extends RedisElement {
    private final String key;
    private final String value;

    public RedisStringElement(String key, String value) {
        super(RedisType.STRING);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
