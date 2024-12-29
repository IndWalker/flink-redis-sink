package org.example.redis_sink;

import org.apache.flink.api.common.JobInfo;
import org.apache.flink.api.common.TaskInfo;
import org.apache.flink.api.common.operators.MailboxExecutor;
import org.apache.flink.api.common.operators.ProcessingTimeService;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.connector.sink2.WriterInitContext;
import org.apache.flink.metrics.groups.SinkWriterMetricGroup;
import org.apache.flink.util.UserCodeClassLoader;

import java.util.OptionalLong;

public class RedisSinkWriterInitContext implements WriterInitContext {
    @Override
    public UserCodeClassLoader getUserCodeClassLoader() {
        return null;
    }

    @Override
    public MailboxExecutor getMailboxExecutor() {
        return null;
    }

    @Override
    public ProcessingTimeService getProcessingTimeService() {
        return null;
    }

    @Override
    public SinkWriterMetricGroup metricGroup() {
        return null;
    }

    @Override
    public SerializationSchema.InitializationContext asSerializationSchemaInitializationContext() {
        return null;
    }

    @Override
    public boolean isObjectReuseEnabled() {
        return false;
    }

    @Override
    public <IN> TypeSerializer<IN> createInputSerializer() {
        return null;
    }

    @Override
    public OptionalLong getRestoredCheckpointId() {
        return OptionalLong.empty();
    }

    @Override
    public JobInfo getJobInfo() {
        return null;
    }

    @Override
    public TaskInfo getTaskInfo() {
        return null;
    }
}
