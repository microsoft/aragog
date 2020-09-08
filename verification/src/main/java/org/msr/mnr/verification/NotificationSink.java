package org.msr.mnr.verification;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class NotificationSink implements SinkFunction<Notification> {
    private static final long serialVersionUID = 1L;

    @Override
    public void invoke(Notification n) {
        
    }
}
