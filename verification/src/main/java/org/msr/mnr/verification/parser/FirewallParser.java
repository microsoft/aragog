package org.msr.mnr.verification.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.ParseIntArray;

import java.text.NumberFormat;
import java.util.Locale;
import java.text.ParseException;

public class FirewallParser implements FlatMapFunction<String, Packet> {
    private static final long serialVersionUID = -5308869395657847239L;
    private static final NumberFormat format = NumberFormat.getInstance(Locale.US);

    @Override
    public void flatMap(String data, Collector<Packet> out) throws IOException {
        if(data.startsWith("#") || data.length() == 0 || data.startsWith("time")) {
            return;
        }
        // System.out.println(data);
        System.out.print(System.currentTimeMillis()/1000.0);
        System.out.println(",1");

        // Remove this Try Catch to see the issue
        // try {
        Packet p = new Packet();
        String[] tokens = data.toLowerCase().split(";");
        int index = 0;

        p.setTime(Math.round(Double.parseDouble(tokens[index++]) * 1000.0));    
        // index++;

        p.set("event_type", ParseIntArray.fromString(tokens[index++]));
        // p.set("proto", tokens[index].getBytes(), 0, tokens[index++].length());
        p.set("transport_protocol", ParseIntArray.fromString(tokens[index++]));

        // if (tokens[index].length() > 0) {
        //     p.set("flow_id", ParseIntArray.fromString(tokens[index++]));
        // } else {
        //     index++;
        // }
        if (tokens[index].length() > 0) {
            p.set("flow_state", ParseIntArray.fromString(tokens[index++]));
        } else {
            index++;
        }

        p.set("srcIp", tokens[index].getBytes(), 0, tokens[index++].length());
        p.set("dstIp", tokens[index].getBytes(), 0, tokens[index++].length());


        if (tokens[index].length() > 0) {
            p.set("srcL4Port", ParseIntArray.fromString(tokens[index++]));
        } else {
            index++;
        }

        if (tokens[index].length() > 0) {
            p.set("dstL4Port", ParseIntArray.fromString(tokens[index++]));
        } else {
            index++;
        }

        // if (tokens[index].length() > 0) {
        //     p.set("replied", tokens[index].getBytes(), 0, tokens[index++].length());
        // } else {
        //     index++;
        // }

        // p.set("reverse_srcIp", tokens[index].getBytes(), 0, tokens[index++].length());
        // p.set("reverse_dstIp", tokens[index].getBytes(), 0, tokens[index++].length());


        // if (tokens[index].length() > 0) {
        //     p.set("reverse_type", ParseIntArray.fromString(tokens[index++]));
        // } else {
        //     index++;
        // }

        // if (tokens[index].length() > 0) {
        //     p.set("reverse_code", ParseIntArray.fromString(tokens[index++]));
        // } else {
        //     index++;
        // }

        // if (tokens[index].length() > 0) {
        //     p.set("reverse_id", ParseIntArray.fromString(tokens[index++]));
        // } else {
        //     index++;
        // }

        // if (tokens[index].length() > 0) {
        //     p.set("reverse_srcL4Port", ParseIntArray.fromString(tokens[index++]));
        // } else {
        //     index++;
        // }

        // if (tokens[index].length() > 0) {
        //     p.set("reverse_dstL4Port", ParseIntArray.fromString(tokens[index++]));
        // } else {
        //     index++;
        // }

        // if (tokens[index].length() > 0) {
        //     p.set("assured", tokens[index].getBytes(), 0, tokens[index++].length());
        // } else {
        //     index++;
        // }
        p.setLocation(ParseIntArray.fromString(tokens[index++]));
        out.collect(p);
        
    }
}
