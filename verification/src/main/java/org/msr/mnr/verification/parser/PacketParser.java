package org.msr.mnr.verification.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class PacketParser implements Serializable {
    private static final long serialVersionUID = 1L;
    private ParseNode root = null;
    
    public PacketParser(String formatFile) {
        JSONParser jsonParser = new JSONParser();
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
                last = new Field(entry.getKey(), entry.getValue(), last);
            } else {
                throw new IllegalArgumentException("Empty field object?");
            }

            if (first == null) {
                first = last;
            }
        }

        return first;
    }

    public void parsePacket(DatagramPacket dp, Packet p) {
        parsePacketHelper(dp.getData(), 0, p, root);
    }

    public void parsePacket(String input, Packet p) {
        byte[] data = new byte[input.length()];
        for (int i = 0; i < input.length(); ++i) {
            data[i] = Byte.parseByte(input.substring(i, i+1), 16);
        }
        parsePacketHelper(data, 0, p, root);
    }

    private int parsePacketHelper(byte[] data, int index, Packet p, ParseNode current) {
        int nextIndex = index;
        if (current instanceof Field) {
            // If it's a simple Field, parse it and set nextIndex
            Field f = (Field) current;
            nextIndex = index + f.length;
            p.set(f.name, data, index, nextIndex);
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

    // ===========================================================
    // =================   Helper classes  =======================
    // ===========================================================
    
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
        public int length;
        
        Field(Object name, Object length, ParseNode last) {
            super(last);
            this.name = (String) name;
            this.length = ((Long) length).intValue();
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
                } else {
                    value = ParseIntArray.fromLong(Long.parseLong(conditionTokens[1]));
                }
            }
        }
    }
}
