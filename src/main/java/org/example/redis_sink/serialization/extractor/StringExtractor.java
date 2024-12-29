package org.example.redis_sink.serialization.extractor;

import org.apache.flink.api.java.tuple.Tuple2;

public class StringExtractor implements Extractor {

    @Override
    public String extractKey(Tuple2<String, Object> element) {
        return element.f0;
    }

    @Override
    public String extractValue(Tuple2<String, Object> element) {
        return element.f1.toString();
    }
}
