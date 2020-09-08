package org.msr.mnr.verification.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

// Utilities to convert to an int array.  Much of this was taken from Java's BigInteger
public class ParseIntArray {
    public static int[] fromBytes(byte[] data, int start, int end) {
        int[] value;

        // Find first nonzero byte
        int byteLength = end - start;
        int keep;
        for (keep = 0; keep < byteLength && data[start + keep] == 0; ++keep) {}

        // Allocate new array and copy relevant part of input array
        int intLength = (byteLength - keep + 3) >>> 2;
        value = new int[intLength];
        
        int b = end - 1;
        for (int i = intLength - 1; i >= 0; --i) {
            value[i] = data[b--] & 0xff;
            int bytesRemaining = b - keep + 1;
            int bytesToTransfer = Math.min(3, bytesRemaining);
            for (int j = 8; j <= (bytesToTransfer << 3); j += 8) {
                value[i] |= ((data[b--] & 0xff) << j);
            }
        }

        return value;
    }

    public static int[] fromHexString(String data) {
        int value[];
        int cursor = 0;
        final int len = data.length();

        // Skip leading zeros and compute number of digits in magnitude
        while (cursor < len && data.charAt(cursor) == '0') {
            ++cursor;
        }
        if (cursor == len) {
            return new int[0];
        }
        int numDigits = len - cursor;

        // Pre-allocate array of expected size. May be too large but can
        // never be too small. Typically exact.
        int intLength = (numDigits + 7) >>> 3;
        value = new int[intLength];

        // Process first (potentially short) digit group
        int firstGroupLen = numDigits % 8;
        if (firstGroupLen == 0) {
            firstGroupLen = 8;
        }
        String group = data.substring(cursor, cursor += firstGroupLen);
        int valueCursor = intLength;
        value[--valueCursor] = Integer.parseUnsignedInt(group, 16);

        // Process remaining digit groups
        while (cursor < len) {
            group = data.substring(cursor, cursor += 8);
            value[--valueCursor] = Integer.parseUnsignedInt(group, 16);
        }
        // Required for cases where the array was overallocated.
        return valueCursor == 0 ? value : Arrays.copyOfRange(value, valueCursor, intLength);
    }

    public static int[] fromString(String data) {
        int value[];
        int cursor = 0;
        final int len = data.length();

        // Skip leading zeros and compute number of digits in magnitude
        while (cursor < len && data.charAt(cursor) == '0') {
            ++cursor;
        }
        if (cursor == len) {
            return new int[1];
        }

        int numDigits = len - cursor;

        // Pre-allocate array of expected size
        int numWords;
        if (len < 10) {
            numWords = 1;
        } else {
            long numBits = ((numDigits * 3402) >>> 10) + 1;
            if (numBits + 31 >= (1L << 32)) {
                throw new ArithmeticException("BigInteger would overflow supported range");
            }
            numWords = (int) (numBits + 31) >>> 5;
        }
        value = new int[numWords];

        // Process first (potentially short) digit group
        int firstGroupLen = numDigits % 9;
        if (firstGroupLen == 0) {
            firstGroupLen = 9;
        }
        // System.out.println("DATA: " + data);
        // System.out.println("cursor: " + Integer.toString(cursor));
        // System.out.println("numDigits: " + Integer.toString(numDigits));
        // System.out.println("firstGroupLen: " + Integer.toString(firstGroupLen));
        // System.out.println("len: " + Integer.toString(len));
        value[numWords - 1] = parseInt(data, cursor, cursor += firstGroupLen);

        // Process remaining digit groups
        while (cursor < len) {
            int groupVal = parseInt(data, cursor, cursor += 9);
            destructiveMulAdd(value, 0x3b9aca00, groupVal);
        }

        // Remove any leading zeros
        int vlen = value.length;
        int keep;
        for (keep = 0; keep < vlen && value[keep] == 0; keep++)
            ;
        if (keep != 0) {
            value = java.util.Arrays.copyOfRange(value, keep, vlen);
        }

        return value;
    }

    private static int parseInt(String s, int start, int end) {
        int result = Character.digit(s.charAt(start++), 10);
        if (result == -1) {
            throw new NumberFormatException(s);
        }

        for (int index = start; index < end; index++) {
            int nextVal = Character.digit(s.charAt(index), 10);
            if (nextVal == -1) {
                throw new NumberFormatException(s);
            }
            result = 10 * result + nextVal;
        }

        return result;
    }

    final static long LONG_MASK = 0xffffffffL;
    private static void destructiveMulAdd(int[] x, int y, int z) {
        // Perform the multiplication word by word
        long ylong = y & LONG_MASK;
        long zlong = z & LONG_MASK;
        int len = x.length;

        long product = 0;
        long carry = 0;
        for (int i = len - 1; i >= 0; i--) {
            product = ylong * (x[i] & LONG_MASK) + carry;
            x[i] = (int) product;
            carry = product >>> 32;
        }

        // Perform the addition
        long sum = (x[len - 1] & LONG_MASK) + zlong;
        x[len - 1] = (int) sum;
        carry = sum >>> 32;
        for (int i = len - 2; i >= 0; i--) {
            sum = (x[i] & LONG_MASK) + carry;
            x[i] = (int) sum;
            carry = sum >>> 32;
        }
    }

    public static int[] fromLong(long data) {
        int[] value;

        int highWord = (int) (data >>> 32);
        if (highWord == 0) {
            value = new int[1];
            value[0] = (int) data;
        } else {
            value = new int[2];
            value[0] = highWord;
            value[1] = (int) data;
        }

        return value;
    }

    public static int compare(int[] left, int[] right) {
        if (left.length < right.length) {
            return -1;
        } else if (left.length > right.length) {
            return 1;
        }

        for (int i = 0; i < left.length; ++i) {
            if (left[i] < right[i]) {
                return -1;
            } else if (left[i] > right[i]) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Returns the number of bits in the minimal two's-complement representation of
     * this BigInteger, <i>excluding</i> a sign bit. For positive BigIntegers, this
     * is equivalent to the number of bits in the ordinary binary representation.
     * (Computes {@code (ceil(log2(this < 0 ? -this : this+1)))}.)
     *
     * @return number of bits in the minimal two's-complement representation of this
     *         BigInteger, <i>excluding</i> a sign bit.
     */
    private static int bitLength(int[] array) {
        int n;
        int len = array.length;
        if (len == 0) {
            n = 0; // offset by one to initialize
        } else {
            // Calculate the bit length of the magnitude
            int magBitLength = ((len - 1) << 5) + 32 - Integer.numberOfLeadingZeros(array[0]);
            n = magBitLength;
        }
        return n;
    }

    /**
     * Returns the specified int of the little-endian two's complement
     * representation (int 0 is the least significant). The int number can be
     * arbitrarily high (values are logically preceded by infinitely many sign
     * ints).
     */
    private static int getInt(int[] array, int n) {
        if (n < 0 || n >= array.length) {
            return 0;
        }

        return array[array.length - n - 1];
    }

    public static String printString(int[] array) {
        if (array == null) {
            return "null";
        }
        int byteLen = bitLength(array) / 8 + 1;
        byte[] byteArray = new byte[byteLen];

        for (int i = byteLen - 1, bytesCopied = 4, nextInt = 0, intIndex = 0; i >= 0; i--) {
            if (bytesCopied == 4) {
                nextInt = getInt(array, intIndex++);
                bytesCopied = 1;
            } else {
                nextInt >>>= 8;
                bytesCopied++;
            }
            byteArray[i] = (byte)nextInt;
        }
        
        BigInteger bi = new BigInteger(byteArray);
        return bi.toString();
    }


    public static long getLong(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array empty in getLong");
        }
        int byteLen = bitLength(array) / 8 + 1;
        byte[] byteArray = new byte[byteLen];

        for (int i = byteLen - 1, bytesCopied = 4, nextInt = 0, intIndex = 0; i >= 0; i--) {
            if (bytesCopied == 4) {
                nextInt = getInt(array, intIndex++);
                bytesCopied = 1;
            } else {
                nextInt >>>= 8;
                bytesCopied++;
            }
            byteArray[i] = (byte)nextInt;
        }
        return toLong(byteArray);
    }

    private static long toLong(byte[] value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        final byte val = (byte) (value[0] < 0 ? 0xFF : 0);

        for(int i = value.length; i < Long.BYTES; i++)
            buffer.put(val);

        buffer.put(value);
        return buffer.getLong(0);
    }

    public static String getString(int[] values) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        for(int i=0; i < values.length; ++i)
        {
            try {
                dos.writeInt(values[i]);
            } catch(IOException e) {
                System.out.println("Unable to convert to String"); 
            }
        }

        return new String(baos.toByteArray());
    }
}
