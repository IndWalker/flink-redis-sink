package org.example.redis_sink;

import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.api.connector.sink2.SinkWriter;
import org.apache.flink.api.connector.sink2.WriterInitContext;
import org.apache.flink.api.java.tuple.Tuple2;
import org.example.redis_sink.serialization.schema.RedisSerializationSchema;
import org.example.redis_sink.utils.RedisOptions;

import java.io.IOException;

public class RedisSink<IN> implements Sink<IN> {

    private final RedisOptions redisOptions;
    private final RedisSerializationSchema<Tuple2<String, Object>> serializationSchema;

    public RedisSink(
            RedisOptions redisOptions,
            RedisSerializationSchema<Tuple2<String, Object>> serializationSchema) {
        this.redisOptions = redisOptions;
        this.serializationSchema = serializationSchema;
    }

//    Implemented for compatibility
    public SinkWriter<IN> createWriter(InitContext context) throws IOException {
        return null;
    }

    @Override
    public SinkWriter<IN> createWriter(WriterInitContext context) throws IOException {
        return new RedisWriter<>(context, redisOptions, serializationSchema);
    }
}
