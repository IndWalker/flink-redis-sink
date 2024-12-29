package org.example;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.example.accumulators.TemperatureAccumulator;
import org.example.redis_sink.utils.ParseRedisInput;
import org.example.redis_sink.serialization.schema.RedisHsetSerializationSchema;
import org.example.redis_sink.utils.RedisOptions;
import org.example.redis_sink.RedisSink;

import java.time.Duration;
import java.util.*;

public class Main {
    private final static String HOST = "localhost";
    private final static int PORT = 6379;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(1000);

        GeneratorFunction<Long, Long> generatorFunction = index -> index;

        DataGeneratorSource<Long> source = new DataGeneratorSource<>(generatorFunction, Long.MAX_VALUE,
                RateLimiterStrategy.perSecond(10), Types.LONG);

        DataStream<Long> stream = env.fromSource(
                source,
//                Just assign watermark based on current timestamp since the source is data generator
                WatermarkStrategy.<Long>forMonotonousTimestamps().withTimestampAssigner((record, event) -> new Date().getTime()),
                "Source");

        DataStream<Tuple2<String, Map<String, String>>> stream2 = stream.map(
                new MapFunction<Long, Tuple2<String, Map<String, String>>>() {
            @Override
            public Tuple2<String, Map<String, String>> map(Long value) throws Exception {

                HashMap<String, String> measurements = new HashMap<>();

                String key = "truck:" + TemperatureSensorRecord.getTruckNumber();

                measurements.put("temperature", String.valueOf(TemperatureSensorRecord.getTemperature()));
                measurements.put("event-time", String.valueOf(new Date().getTime()));

                return new Tuple2<>(key, measurements);
            }
        });

        DataStream<Tuple2<String, Map<String, String>>> stream3 = stream2.keyBy(new KeySelector<Tuple2<String, Map<String, String>>, String>() {
            @Override
            public String getKey(Tuple2<String, Map<String, String>> record) throws Exception {
                return record.f0;
            }
        }).window(TumblingEventTimeWindows.of(Duration.ofMillis(5000)))
                .aggregate(new AggregateFunction<Tuple2<String, Map<String, String>>, TemperatureAccumulator, Tuple2<String, Map<String, String>>>() {
                    @Override
            public TemperatureAccumulator createAccumulator() {
                return new TemperatureAccumulator(null,0, 0);
            }

            @Override
            public TemperatureAccumulator add(Tuple2<String, Map<String, String>> record, TemperatureAccumulator o) {
                if (o.getKey() == null) {
                    o.setKey(record.f0);
                }
                o.setCountInstances(o.getCountInstances() + 1);

                int temperature = Integer.parseInt(record.f1.get("temperature"));
                o.setCountTemperature(o.getCountTemperature() + temperature);

                return o;
            }

            @Override
            public Tuple2<String, Map<String, String>> getResult(TemperatureAccumulator o) {
                Map<String, String> measurements = new HashMap<>();
                measurements.put("temperature", String.valueOf(o.getCountTemperature() / o.getCountInstances()));
                measurements.put("trucks-measured", String.valueOf(o.getCountInstances()));
                return new Tuple2<>(o.getKey(), measurements);
            }

            @Override
            public TemperatureAccumulator merge(TemperatureAccumulator o, TemperatureAccumulator acc1) {
                int totalTemp = o.getCountTemperature() + acc1.getCountTemperature();
                int totalInstances = o.getCountInstances() + acc1.getCountInstances();
                return new TemperatureAccumulator(o.getKey(), totalTemp, totalInstances);
            }
        });


        RedisSink<Tuple2<String, Map<String, String>>> sink = new RedisSink<>(
                new RedisOptions(HOST, PORT),
                new RedisHsetSerializationSchema<>(ParseRedisInput::parse)
        );
        stream3.sinkTo(sink);

        stream3.print();

        env.execute();
    }

//    Generating random truck sensor data
    private static class TemperatureSensorRecord {

        private static final Random random = new Random();

        static int getTruckNumber() {
            return random.nextInt(100);
        }

        static int getTemperature() {
            return random.nextInt(50);
        }
    }
}
