package com.BrunoRebic;

public class IndexRecord {
    private final int startByte;
    private final int length;
    private final int descriptionLength;
    private final int exitsLength;

    public IndexRecord(int startByte, int length,int descriptionLength,int exitsLength) {
        this.startByte = startByte;
        this.length = length;
        this.descriptionLength = descriptionLength;
        this.exitsLength = exitsLength;
    }

    public int getStartByte() {
        return startByte;
    }

    public int getLength() {
        return length;
    }

    public int getDescriptionLength() {
        return descriptionLength;
    }

    public int getExitsLength() {
        return exitsLength;
    }
}
