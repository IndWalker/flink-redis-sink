package org.example.redis_sink.serialization.schema;

import org.apache.flink.api.common.serialization.SerializationSchema;

import org.apache.flink.api.java.tuple.Tuple2;
import org.example.redis_sink.serialization.element.RedisElement;
import org.example.redis_sink.utils.RedisOptions;

import java.io.Serializable;

public interface RedisSerializationSchema<IN extends Tuple2<String, Object>> extends Serializable {

    default void open(
            SerializationSchema.InitializationContext initContext,
            RedisOptions options) throws Exception {

    }

    RedisElement serialize(IN element);
}
