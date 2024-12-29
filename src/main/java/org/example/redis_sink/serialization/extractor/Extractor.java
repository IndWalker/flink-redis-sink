package org.example.redis_sink.serialization.extractor;

import org.apache.flink.api.java.tuple.Tuple2;

public interface Extractor {
    String extractKey(Tuple2<String, Object> element);
    Object extractValue(Tuple2<String, Object> element);
}
