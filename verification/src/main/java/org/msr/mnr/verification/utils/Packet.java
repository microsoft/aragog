package org.msr.mnr.verification.utils;

import java.util.Map;
import java.util.TreeMap;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Packet implements Comparable<Packet> {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("HH:mm:ss").appendFraction(ChronoField.MICRO_OF_SECOND, 6, 6, true)
            .toFormatter();

    private Map<String, int[]> fieldMap;
    private long time;
    private int[] location;

    public Packet() {
        fieldMap = new TreeMap<String, int[]>();
    }

    public Packet(int[] location, Map<String, int[]>  fieldMap, long time) {
        this.fieldMap = fieldMap;
        this.time = time;
        this.location = location;
    }

    public void set(String field, int[] data) {
        fieldMap.put(field, data);
    }

    public void set(String field, byte[] data, int start, int end) {
        fieldMap.put(field, ParseIntArray.fromBytes(data, start, end));
    }

    public int[] get(Object field) {
        if (!fieldMap.containsKey(field)) {
            System.out.println("WARNING: unknown field " + field);
            return new int[0];
        }
        return fieldMap.get(field);
    }

    @Override
    public String toString() {
        String output;
        output = "(t" + time + "), location: " + Arrays.toString(location) + ",";
        for (Map.Entry<String, int[]> entry : fieldMap.entrySet()) {
            if (containsIgnoreCase(entry.getKey() , "ip")) {
                output += ", " + entry.getKey() + ": " + ParseIntArray.getString(entry.getValue());
            } else if (containsIgnoreCase(entry.getKey() , "entry_sequence_number")) {
                output += ", " + entry.getKey() + ": " + ParseIntArray.printString(entry.getValue());
            } else {
                output += ", " + entry.getKey() + ": " + Arrays.toString(entry.getValue());
            }
        }
        // System.out.println(output);
        output = output.replace("\0", "");
        return output;
    }

    public void setTime(String time) {
        LocalTime lt = LocalTime.parse(time, FORMATTER);
        this.time = lt.atDate(LocalDate.now()).getLong(ChronoField.MICRO_OF_DAY);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, int[]> getFieldMap() {
        return fieldMap;
    }

    public long getTime() {
        return time;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public int[] getLocation() {
        return location;
    }

    // Returns: the comparator value, negative if less, positive if greater
    @Override
    public int compareTo(Packet o) {
        return Long.compare(time, o.time);
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}
