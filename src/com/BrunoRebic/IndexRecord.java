package com.BrunoRebic;

public class IndexRecord {
    private final int startByte;
    private final int length;

    public IndexRecord(int startByte, int length) {
        this.startByte = startByte;
        this.length = length;
    }

    public int getStartByte() {
        return startByte;
    }

    public int getLength() {
        return length;
    }
}
