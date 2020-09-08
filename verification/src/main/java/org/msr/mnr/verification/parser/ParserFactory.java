package org.msr.mnr.verification.parser;

import org.msr.mnr.verification.utils.Packet;

import org.apache.flink.api.common.functions.FlatMapFunction;

public class ParserFactory {
    public static FlatMapFunction<String, Packet> createNewParser(String parserType){
        if(parserType == null){
            return null;
        } else if(parserType.equalsIgnoreCase("firewall")){
            return new FirewallParser();
        }
         
        return null;
    }
}