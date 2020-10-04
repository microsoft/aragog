package org.msr.mnr.verification.parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.ParseIntArray;
 
public class GenericParser implements FlatMapFunction<String, Packet>  {
    private static final long serialVersionUID = 1L;
    private ParseNode root = null;
    private String delimiter;
    
    public GenericParser(String formatFile, String _delimiter) {
        JSONParser jsonParser = new JSONParser();
        this.delimiter = _delimiter;
        try (FileReader reader = new FileReader(formatFile)) {
            JSONObject config = (JSONObject) jsonParser.parse(reader);

            // Parse the packet format
            JSONArray fieldFormat = (JSONArray) config.get("fields");
            root = parseFormat(fieldFormat, null);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private ParseNode parseFormat(JSONArray fieldFormat, ParseNode last) {
        ParseNode first = null;

        for (Object nodeObj : fieldFormat) {
            JSONObject node = (JSONObject) nodeObj;

            if (node.size() > 1) {
                ConditionalNode cn = new ConditionalNode(last);

                for (Object conditionStr : node.keySet()) {
                    Condition cond = new Condition((String) conditionStr);
                    JSONArray child = (JSONArray) node.get(conditionStr);
                    cn.children.put(cond, parseFormat(child, null));
                }

                last = cn;
            } else if (node.size() == 1) {
                Object entryObj = node.entrySet().iterator().next();
                Map.Entry<?, ?> entry = Map.Entry.class.cast(entryObj);
                last = new Field(entry.getKey(), last);
            } else {
                throw new IllegalArgumentException("Empty field object?");
            }

            if (first == null) {
                first = last;
            }
        }

        return first;
    }

     @Override
    public void flatMap(String data, Collector<Packet> out) throws IOException {
        if(data.startsWith("#") || data.length() == 0 || data.startsWith("time")) {
            return;
        }
        // System.out.println("Input line: " + data);
        Packet p = new Packet();
        String[] inputLine = data.split(delimiter);
        parsePacketHelper(inputLine, 0, p, root);
        out.collect(p);
    }

    private int parsePacketHelper(String[] data, int index, Packet p, ParseNode current) {
        int nextIndex = index;
        if (current instanceof Field) {
            // If it's a simple Field, parse it and set nextIndex
            Field f = (Field) current;
            nextIndex += 1;
            // System.out.println("Name: " +  f.name);
            if (f.name.equals("time")) {
                if (isNumeric(data[index])) {
                    p.setTime(Math.round(Double.parseDouble(data[index]) * 1000.0));
                } else {
                    p.setTime(data[index]);
                }
            } else if (isNumeric(data[index])) {
                p.set(f.name, ParseIntArray.fromString(data[index]));
            } else {
                p.set(f.name, data[index].getBytes(), 0, data[index].length());
            }
        } else {
            // If it's a conditional, traverse the correct child
            ConditionalNode cn = (ConditionalNode) current;
            ParseNode child = cn.getChildNode(p);

            if (child != null) {
                nextIndex = parsePacketHelper(data, index, p, child);
            }
        }

        if (current.next != null) {
            nextIndex = parsePacketHelper(data, nextIndex, p, current.next);
        }
        return nextIndex;
    }

    private abstract class ParseNode implements Serializable {
        private static final long serialVersionUID = 1L;
        public ParseNode next;

        ParseNode(ParseNode last) {
            if (last != null) {
                last.next = this;
            }
        }
    }

    private class Field extends ParseNode {
        private static final long serialVersionUID = 1L;
        public String name;
        
        Field(Object name, ParseNode last) {
            super(last);
            this.name = (String) name;
        }
    }

    private class ConditionalNode extends ParseNode {
        private static final long serialVersionUID = 1L;
        LinkedHashMap<Condition, ParseNode> children = new LinkedHashMap<Condition, ParseNode>();

        ConditionalNode(ParseNode last) {
            super(last);
        }

        ParseNode getChildNode(Packet p) {
            for (Condition c : children.keySet()) {
                if (c.field == null) {
                    return children.get(c);
                }
                if (p.get(c.field).equals(c.value)) {
                    return children.get(c);
                }
            }

            return null;
        }
    }

    private class Condition implements Serializable {
        private static final long serialVersionUID = 1L;
        String field;
        int[] value;

        Condition(String condition) {
            if (condition.equalsIgnoreCase("default")) {
                field = null;
                value = null;
            } else {
                String[] conditionTokens = condition.split("=");
                assert (conditionTokens.length == 2);

                field = conditionTokens[0];
                if (conditionTokens[1].startsWith("0x")) {
                    value = ParseIntArray.fromHexString(conditionTokens[1].substring(2));
                } else if (GenericParser.isNumeric(conditionTokens[1])) {
                    value = ParseIntArray.fromLong(Long.parseLong(conditionTokens[1]));
                } else {
                    value = ParseIntArray.fromString(conditionTokens[1]);
                }
            }
        }
    }

    public static boolean isNumeric(String inputStr) {
        if (inputStr == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
