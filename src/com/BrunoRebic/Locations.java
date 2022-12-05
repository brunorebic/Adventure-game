package com.BrunoRebic;

import java.io.*;
import java.util.*;


public class Locations implements Map<Integer, Location> {
    private static final Map<Integer, Location> locations = new LinkedHashMap<>();
    private static final Map<Integer, IndexRecord> index = new LinkedHashMap<>();
    private static RandomAccessFile raf;

    public static void main(String[] args) throws IOException {
        //writing to RandomAccessFile
//        try (RandomAccessFile raf = new RandomAccessFile("locations_rand.dat", "rwd")) {
//            raf.writeInt(locations.size());
//            int locationStart = (int) ((locations.size() * 3 * Integer.BYTES) + Integer.BYTES + raf.getFilePointer());
//            raf.writeInt(locationStart);
//            int indexStart = (int) raf.getFilePointer();
//
//            int filePointer = locationStart;
//            raf.seek(filePointer);
//
//            for (Location location : locations.values()) {
//                raf.writeInt(location.getLocationID());
//                raf.writeUTF(location.getDescription());
//
//                StringBuilder builder = new StringBuilder();
//                for (String description : location.getExits().keySet()) {
//                    if (!description.equalsIgnoreCase("Q")) {
//                        builder.append(description).append(",").append(location.getExits().get(description)).append(",");
//                    }
//                }
//                raf.writeUTF(builder.toString());
//
//                IndexRecord indexRecord = new IndexRecord(filePointer, (int) (raf.getFilePointer() - filePointer));
//                index.put(location.getLocationID(), indexRecord);
//
//                filePointer = (int) raf.getFilePointer();
//            }
//
//            raf.seek(indexStart);
//            for (Integer id : index.keySet()) {
//                raf.writeInt(id);
//                raf.writeInt(index.get(id).getStartByte());
//                raf.writeInt(index.get(id).getLength());
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

        try {
            raf = new RandomAccessFile("locations_rand.dat", "rwd");
            int locSize = raf.readInt();
            int locationStart = raf.readInt();

            while (raf.getFilePointer() < locationStart) {
                int locID = raf.readInt();
                int startByte = raf.readInt();
                int length = raf.readInt();

                IndexRecord indexRecord = new IndexRecord(startByte, length);
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
        raf.seek(startPosition);

        int locationID = raf.readInt();
        String description = raf.readUTF();
        String exits = raf.readUTF();

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
        raf.close();
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
