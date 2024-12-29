package org.example.redis_sink.serialization.schema;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.function.SerializableFunction;
import org.example.redis_sink.serialization.element.RedisElement;
import org.example.redis_sink.serialization.element.RedisHsetElement;
import org.example.redis_sink.utils.RedisOptions;
import org.example.redis_sink.serialization.extractor.HsetExtractor;

import java.util.Map;

public class RedisHsetSerializationSchema<KeyValue extends Tuple2<String, Object>> implements RedisSerializationSchema<KeyValue> {

    private final HsetExtractor extractor;

    public RedisHsetSerializationSchema(SerializableFunction<Object, Map<String, String>> convertValueFunction) {
        this.extractor = new HsetExtractor(convertValueFunction);
    }

    @Override
    public void open(SerializationSchema.InitializationContext initContext, RedisOptions options) throws Exception {
        RedisSerializationSchema.super.open(initContext, options);
    }

    @Override
    public RedisElement serialize(KeyValue element) {
        String key = extractor.extractKey(element);
        Map<String, String> value = extractor.extractValue(element);
        return new RedisHsetElement(key, value);
    }
}
