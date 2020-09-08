package org.msr.mnr.verification.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.PriorityQueue;

import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.common.typeutils.TypeSerializerSnapshot;
import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.DataOutputView;

public class PriorityQueueSerializer extends TypeSerializer<PriorityQueue<Packet>> {
    private static final long serialVersionUID = 1L;
    public static final PriorityQueueSerializer INSTANCE = new PriorityQueueSerializer();

    @Override
    public boolean isImmutableType() {
        return false;
    }

    @Override
    public TypeSerializer<PriorityQueue<Packet>> duplicate() {
        return this;
    }

    @Override
    public PriorityQueue<Packet> createInstance() {
        return new PriorityQueue<Packet>();
    }

    @Override
    public PriorityQueue<Packet> copy(PriorityQueue<Packet> from) {
        return new PriorityQueue<Packet>(from);
    }

    @Override
    public PriorityQueue<Packet> copy(PriorityQueue<Packet> from, PriorityQueue<Packet> reuse) {
        return copy(from);
    }

    @Override
    public int getLength() {
        return -1;
    }

    @Override
    public void serialize(PriorityQueue<Packet> record, DataOutputView target) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(record);

        byte[] rawQueue = baos.toByteArray();
        target.writeInt(rawQueue.length);
        target.write(rawQueue);

        oos.close();
        baos.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public PriorityQueue<Packet> deserialize(DataInputView source) throws IOException {
        int length = source.readInt();

        byte[] rawQueue = new byte[length];
        source.read(rawQueue);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawQueue);
        ObjectInputStream ois = new ObjectInputStream(bais);

        PriorityQueue<Packet> pq = null;
        try {
            pq = (PriorityQueue<Packet>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ois.close();
        bais.close();

        return pq;
    }

    @Override
    public PriorityQueue<Packet> deserialize(PriorityQueue<Packet> reuse, DataInputView source)
            throws IOException {
        return deserialize(source);
    }

    @Override
    public void copy(DataInputView source, DataOutputView target) throws IOException {
        throw new RuntimeException("Unimplemented...");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public TypeSerializerSnapshot<PriorityQueue<Packet>> snapshotConfiguration() {
        return new PriorityQueueSerializerSnapshot();
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
