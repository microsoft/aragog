package org.msr.mnr.verification;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.PacketParser;

/**
 * A source function that reads strings from a socket. The source will read bytes from the socket
 * stream and convert them to characters, each byte individually. When the delimiter character is
 * received, the function will output the current string, and begin a new string.
 *
 * <p>The function strips trailing <i>carriage return</i> characters (\r) when the delimiter is the
 * newline character (\n).
 *
 * <p>The function can be set to reconnect to the server socket in case that the stream is closed on
 * the server side.
 */
public class PacketSocketSource implements SourceFunction<Packet> {
    private static final long serialVersionUID = 1L;

    private final PacketParser parser;
    private final int port;
    private volatile boolean isRunning = true;

    private transient DatagramSocket socket;

    public PacketSocketSource(String formatFile, int port) {
        this.port = port;
        parser = new PacketParser(formatFile);
    }

    @Override
    public void run(SourceContext<Packet> ctx) throws Exception {
        socket = new DatagramSocket(port);
        byte[] receive = new byte[65535];
        DatagramPacket dp = new DatagramPacket(receive, receive.length);

        while (isRunning) {
            Packet p = new Packet();

            socket.receive(dp);
            parser.parsePacket(dp, p);
            ctx.collect(p);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
        socket.close();
    }
}
