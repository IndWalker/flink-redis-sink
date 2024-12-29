package org.example.redis_sink.serialization.schema;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.example.redis_sink.serialization.element.RedisElement;
import org.example.redis_sink.serialization.element.RedisStringElement;
import org.example.redis_sink.serialization.extractor.StringExtractor;
import org.example.redis_sink.utils.RedisOptions;


public class RedisStringSerializationSchema<KeyValue extends Tuple2<String, Object>> implements RedisSerializationSchema<KeyValue> {

    private StringExtractor extractor;

    @Override
    public void open(SerializationSchema.InitializationContext initContext, RedisOptions options) throws Exception {
        RedisSerializationSchema.super.open(initContext, options);
    }

    @Override
    public RedisElement serialize(KeyValue element) {
        String key = extractor.extractKey(element);
        String value = extractor.extractValue(element);
        return new RedisStringElement(key, value);
    }
}
