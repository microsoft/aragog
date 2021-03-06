package org.msr.mnr.verification.parser;

import org.msr.mnr.verification.utils.Packet;

import org.apache.flink.api.common.functions.FlatMapFunction;

public class ParserFactory {
    public static FlatMapFunction<String, Packet> createNewParser(String parserType, String configFile, String delimiter){
        if(parserType == null){
            return null;
        } else if(parserType.equalsIgnoreCase("firewall")){
            return new FirewallParser();
        } else if (parserType.equalsIgnoreCase("generic")){
            return new GenericParser(configFile, delimiter);
        }
         
        return null;
    }
}