package org.example.redis_sink.serialization.extractor;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.function.SerializableFunction;

import java.io.Serializable;
import java.util.Map;

public class HsetExtractor implements Serializable, Extractor {

    private final SerializableFunction<Object, Map<String, String>> convertValue;

    public HsetExtractor(SerializableFunction<Object, Map<String, String>> convertValue) {
        this.convertValue = convertValue;
    }

    public String extractKey(Tuple2<String, Object> element) {
        return element.f0;
    }

    public Map<String, String> extractValue(Tuple2<String, Object> element) {
        return convertValue.apply(element.f1);
    }
}
