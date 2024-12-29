package org.example.redis_sink.serialization.element;

import org.example.redis_sink.serialization.RedisType;

import java.util.Map;


public class RedisHsetElement extends RedisElement {
    private final String key;
    private final Map<String, String> value;

    public RedisHsetElement(String key, Map<String, String> value) {
        super(RedisType.HSET);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
