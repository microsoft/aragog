package org.msr.mnr.verification;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.PacketParser;

class PacketStringSource implements FlatMapFunction<String, Packet> {
    private static final long serialVersionUID = 1L;
    private PacketParser parser;

    private static int time = 0;

    PacketStringSource(String formatFile) {
        parser = new PacketParser(formatFile);
    }

    @Override
    public void flatMap(String data, Collector<Packet> out) {
        Packet p = new Packet();
        //p.setTime(time++);
        parser.parsePacket(data, p);
        out.collect(p);
    }
}
