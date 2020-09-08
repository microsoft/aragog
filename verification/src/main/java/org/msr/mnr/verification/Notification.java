package org.msr.mnr.verification;

import org.msr.mnr.verification.utils.Packet;

public class Notification {
    String name;
    int finalState;
    Packet p;
    long latency;
    
    public Notification(String name, int finalState, Packet p) {
        this.name = name;
        this.finalState = finalState;
        this.p = p;
        this.latency = System.currentTimeMillis() - p.getTime();
    }

    public String toString() {
        return name + ": " + finalState + ", latency: " + Long.toString(this.latency);
    }
}
