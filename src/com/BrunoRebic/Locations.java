package com.BrunoRebic;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/*
 * 0-3 bytes =  locations size
 * 4-7 bytes = locations startByte (index length = 5 * 4 * locations size + (2 integers already written into file)
 * index components for 1 location:
 * id 4 bytes
 * descriptionLength 4 bytes
 * exitsLength 4 bytes
 * startByte 4 bytes
 * length 4 bytes
 * index length = 5 * 4 * locations size(141) = 2828
 * 8-2828 bytes = index record
 * 2828 - = locations record
 * */

public class Locations implements Map<Integer, Location> {
    private static final Map<Integer, Location> locations = new LinkedHashMap<>();
    private static final Map<Integer, IndexRecord> index = new LinkedHashMap<>();
    private static FileInputStream fileInputStream;

    public static void main(String[] args) throws IOException {
        //writing to RandomAccessFile
//        try (FileOutputStream file = new FileOutputStream("locations_rand.dat");
//             FileChannel channel = file.getChannel()) {
//            ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
//
//            buffer.putInt(locations.size());
//            buffer.flip();
//            channel.write(buffer);
//            buffer.flip();
//
//            long locationsStart = ((long) Integer.BYTES * 5 * locations.size()) + channel.position() + Integer.BYTES;
//            buffer.putInt((int) locationsStart);
//            buffer.flip();
//            channel.write(buffer);
//            buffer.flip();
//            int indexStart = (int) channel.position();
//
//            int filePointer = (int) locationsStart;
//            channel.position(filePointer);
//
//            for (Location location : locations.values()) {
//                buffer = ByteBuffer.allocate(Integer.BYTES);
//                buffer.putInt(location.getLocationID());
//                buffer.flip();
//                channel.write(buffer);
//                buffer.flip();
//                buffer = ByteBuffer.wrap(location.getDescription().getBytes());
//                channel.write(buffer);
//                buffer.flip();
//
//                StringBuilder builder = new StringBuilder();
//
//                for (String direction : location.getExits().keySet()) {
//                    if (!direction.equalsIgnoreCase("Q")) {
//                        builder.append(direction).append(",").append(location.getExits().get(direction)).append(",");
//                    }
//                }
//                System.out.println(builder);
//                buffer = ByteBuffer.wrap(builder.toString().getBytes());
//                channel.write(buffer);
//                buffer.flip();
//
//                IndexRecord indexRecord = new IndexRecord(filePointer, (int) (channel.position() - filePointer), location.getDescription().length(), builder.length());
//                index.put(location.getLocationID(), indexRecord);
//                filePointer = (int) channel.position();
//            }
//
//            buffer.flip();
//            channel.position(indexStart);
//            for (int locID : index.keySet()) {
//                buffer = ByteBuffer.allocate(Integer.BYTES);
//                buffer.putInt(locID);
//                buffer.flip();
//                channel.write(buffer);
//                buffer.flip();
//
//                int startByte = index.get(locID).getStartByte();
//                int length = index.get(locID).getLength();
//                int descriptionLength = index.get(locID).getDescriptionLength();
//                int exitsLength = index.get(locID).getExitsLength();
//
//                buffer.putInt(startByte);
//                buffer.flip();
//                channel.write(buffer);
//                buffer.flip();
//                buffer.putInt(length);
//                buffer.flip();
//                channel.write(buffer);
//                buffer.flip();
//                buffer.putInt(descriptionLength);
//                buffer.flip();
//                channel.write(buffer);
//                buffer.flip();
//                buffer.putInt(exitsLength);
//                buffer.flip();
//                channel.write(buffer);
//            }
//        }
    }

    static {
        //reading from binary file (.dat) using ObjectOutputStream
        try (ObjectInputStream locAndDirFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream("locations_big_object_data.dat")))) {
            boolean eof = false;
            while (!eof) {
                try {
                    Location location = (Location) locAndDirFile.readObject();
                    locations.put(location.getLocationID(), location);

                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //reading from locations_rand.dat using java.nio
        try {
            fileInputStream = new FileInputStream("locations_rand.dat");
            FileChannel channel = fileInputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
            channel.read(buffer);
            int locSize = buffer.getInt(0);
            buffer.flip();
            channel.read(buffer);
            int locStart = buffer.getInt(0);

            while (channel.position() < locStart) {
                buffer = ByteBuffer.allocate(Integer.BYTES);
                channel.read(buffer);
                int locID = buffer.getInt(0);
                buffer.flip();
                channel.read(buffer);
                int startByte = buffer.getInt(0);
                buffer.flip();
                channel.read(buffer);
                int length = buffer.getInt(0);
                buffer.flip();
                channel.read(buffer);
                int descriptionLength = buffer.getInt(0);
                buffer.flip();
                channel.read(buffer);
                int exitsLength = buffer.getInt(0);
                buffer.flip();

                IndexRecord indexRecord = new IndexRecord(startByte, length, descriptionLength, exitsLength);
                index.put(locID, indexRecord);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printLocationsAndExits() {
        for (Location location : locations.values()) {
            System.out.println("Location ID : " + location.getLocationID());
            System.out.println("Location description : " + location.getDescription());
            System.out.println("Exits : ");
            for (String direction : location.getExits().keySet()) {
                System.out.println(direction + " - " + location.getExits().get(direction));
            }
            System.out.println();
        }
    }

    public Location getLocation(int locID) throws IOException {
        IndexRecord indexRecord = index.get(locID);

        int startPosition = indexRecord.getStartByte();
        int descriptionLength = indexRecord.getDescriptionLength();
        int exitsLength = indexRecord.getExitsLength();

        FileChannel channel = fileInputStream.getChannel();
        channel.position(startPosition);

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        channel.read(buffer);
        int locationID = buffer.getInt(0);
        buffer.flip();

        byte[] descriptionArray = new byte[descriptionLength];
        buffer = ByteBuffer.wrap(descriptionArray);
        channel.read(buffer);
        String description = new String(buffer.array());

        byte[] exitsArray = new byte[exitsLength];
        buffer = ByteBuffer.wrap(exitsArray);
        channel.read(buffer);
        String exits = new String(buffer.array());

        Location location = new Location(locationID, description);

        String[] data = exits.split(",");
        if (locationID != 0) {
            for (int i = 0; i < data.length; i++) {
                String direction = data[i];
                int destination = Integer.parseInt(data[++i]);

                location.addExit(direction, destination);
            }
        }
        return location;
    }

    public void close() throws IOException {
        fileInputStream.close();
    }

    @Override
    public int size() {
        return locations.size();
    }

    @Override
    public boolean isEmpty() {
        return locations.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return locations.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return locations.containsValue(value);
    }

    @Override
    public Location get(Object key) {
        return locations.get(key);
    }

    @Override
    public Location put(Integer key, Location value) {
        return locations.put(key, value);
    }

    @Override
    public Location remove(Object key) {
        return locations.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Location> m) {

    }

    @Override
    public void clear() {
        locations.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return locations.keySet();
    }

    @Override
    public Collection<Location> values() {
        return locations.values();
    }

    @Override
    public Set<Entry<Integer, Location>> entrySet() {
        return locations.entrySet();
    }
}
