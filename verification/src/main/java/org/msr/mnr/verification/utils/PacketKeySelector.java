package org.msr.mnr.verification.utils;

import org.apache.flink.api.java.functions.KeySelector;
import org.msr.mnr.verification.utils.Packet;

public class PacketKeySelector implements KeySelector<Packet, String> {
    String filename;
    PacketKeySelector(String _filename) {
        super();
        filename = _filename;
    }

    @Override
    public String getKey(Packet p) throws Exception {
        System.out.println("PacketKey is set to: " + filename);
        return filename;
    }
}