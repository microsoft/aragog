package org.msr.mnr.verification.utils;

import org.apache.flink.api.common.functions.Partitioner;

public class MyPartitioner implements Partitioner<String> {
    @Override
    public int partition(String key, int numPartitions) {
        // System.out.println("incoming key is: " + key);
        int out = key.hashCode() % numPartitions;
        
        if (out < 0) {
            out = -out;
        }
        System.out.println("Input is: " + key + " Output is: " + Integer.toString(out));
        return out;
    }
}