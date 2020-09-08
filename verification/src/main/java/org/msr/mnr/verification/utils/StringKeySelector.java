package org.msr.mnr.verification.utils;

import org.apache.flink.api.java.functions.KeySelector;
import org.msr.mnr.verification.utils.Packet;

public class StringKeySelector implements KeySelector<String, String> {
    String filename;
    public StringKeySelector(String _filename) {
        super();
        filename = _filename;
    }

    @Override
    public String getKey(String p) throws Exception {
        // System.out.println("StringKey is set to: " + filename);
        return filename;
    }
}