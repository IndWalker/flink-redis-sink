package org.example.redis_sink;

import org.apache.commons.math3.util.Pair;
import org.apache.flink.api.connector.sink2.SinkWriter;
import org.apache.flink.api.connector.sink2.WriterInitContext;
import org.apache.flink.api.java.tuple.Tuple2;
import org.example.redis_sink.serialization.element.RedisElement;
import org.example.redis_sink.serialization.schema.RedisSerializationSchema;
import org.example.redis_sink.serialization.RedisType;
import org.example.redis_sink.utils.RedisOptions;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RedisWriter<IN> implements SinkWriter<IN> {

    private final JedisPool jedisPool;
    private final Pipeline pipeline;

    private final int batchSize = 1000;
    private final int maxWaitMs = 750;

    private List<Pair<String, Object>> buffer;
    private final RedisSerializationSchema<Tuple2<String, Object>> serializationSchema;

    public RedisWriter(
            WriterInitContext context,
            RedisOptions redisOptions,
            RedisSerializationSchema<Tuple2<String, Object>> serializationSchema
    ) {
        this.jedisPool = new JedisPool(redisOptions.getHost(), redisOptions.getPort());
        this.pipeline = this.jedisPool.getResource().pipelined();
        this.serializationSchema = serializationSchema;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(IN element, Context context) throws IOException, InterruptedException {
        RedisElement serializedValue = serializationSchema.serialize((Tuple2<String, Object>) element);
        if (serializedValue.getType() == RedisType.HSET) {
            pipeline.hset(serializedValue.getKey(), (Map<String, String>) serializedValue.getValue());
        }
    }

    @Override
    public void flush(boolean endOfInput) throws IOException, InterruptedException {
        pipeline.sync();
    }

    @Override
    public void close() throws Exception {
        this.jedisPool.close();
    }
}
