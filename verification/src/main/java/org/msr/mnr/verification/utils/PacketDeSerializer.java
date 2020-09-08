package org.msr.mnr.verification.utils;

import java.util.Map;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.UnsupportedEncodingException;

public class PacketDeSerializer implements DeserializationSchema<Packet>, SerializationSchema<Packet> {
    
    private static final long serialVersionUID = 1L;

    @Override
    public byte[] serialize(Packet p) {
        JSONObject packetJSON = new JSONObject();
        packetJSON.put("time", p.getTime());
        int[] locations = p.getLocation();

        JSONArray locationJson  = new JSONArray();

        for (int loc : locations) {
            locationJson.add(loc);
        }
        packetJSON.put("locations", locationJson);

        JSONObject jsonFieldMap = new JSONObject();
        Map<String, int[]> fieldMap = p.getFieldMap();

        for (Map.Entry<String, int[]> field_value : fieldMap.entrySet()) {
            int[] values = field_value.getValue();
            JSONArray valueJson  = new JSONArray();
            for (int val : values) {
                valueJson.add(val);
            }
            jsonFieldMap.put(field_value.getKey(), valueJson);
        }

        packetJSON.put("fieldMap", jsonFieldMap);
        byte[] ret = null;
        try {
            ret = packetJSON.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    @Override
    public Packet deserialize(byte[] message) {
        JSONParser parser = new JSONParser();
        JSONObject packetJSON = null;
        System.out.print(System.currentTimeMillis());
        System.out.println(",1");
        try {
            packetJSON = (JSONObject) parser.parse(new String(message));
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        JSONArray locationJson = (JSONArray) packetJSON.get("locations");

        long time = (long) packetJSON.get("time");
        int[] locations = new int[locationJson.size()];
        Map<String, int[]> fieldMap = new TreeMap<String, int[]>();

        int i = 0;
        for(Object loc : locationJson){
            locations[i] = ((Long) loc).intValue();
            i++;
        }

        JSONObject jsonFieldMap = (JSONObject) packetJSON.get("fieldMap");

        for (Object key : jsonFieldMap.keySet()) {
            //based on you key types
            String keyStr = (String) key;
            JSONArray jsonValues = (JSONArray) jsonFieldMap.get(keyStr);
            ArrayList<Integer> values = new ArrayList<>();

            for (Object val : jsonValues) {
                values.add(((Long) val).intValue());
            }
            int[] output = new int[values.size()];
            i = 0;
            for (i = 0; i< values.size(); i++) {
                output[i] = values.get(i);
            }

            fieldMap.put(keyStr, output);

        }

        return new Packet(locations, fieldMap, time);
    }

    @Override
    public boolean isEndOfStream(Packet nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Packet> getProducedType() {
        return TypeInformation.of(Packet.class);
    }
}