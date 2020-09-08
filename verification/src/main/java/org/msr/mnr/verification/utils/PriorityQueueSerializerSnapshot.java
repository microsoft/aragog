package org.msr.mnr.verification.utils;

import java.util.PriorityQueue;

import org.apache.flink.api.common.typeutils.SimpleTypeSerializerSnapshot;

public class PriorityQueueSerializerSnapshot
        extends SimpleTypeSerializerSnapshot<PriorityQueue<Packet>> {

    public PriorityQueueSerializerSnapshot() {
        super(() -> PriorityQueueSerializer.INSTANCE);
    }
}
