package com.BrunoRebic;

import java.io.*;
import java.util.*;

/*
* Main difference between ObjectOutput(Input)Stream and DataOutput(Input)Stream is that using Data stream we need to read every single data type one by one
* and in Object stream we just need to pass the Object and to Serialization to put it in file
*
* Serialization is the process of converting data types into stream of bytes to be able to use it in external database,file of memory
* Recommended to put private final long field serialVersion so that compiler knows what version of serialization is using
*
*
* to conclude this, in this example we are saving every data type of Location class one by one into file using Data stream and vice versa,
* but using Object stream we are saving whole Location class into file and vice versa
* */

public class Locations implements Map<Integer, Location> {
    private static final Map<Integer, Location> locations = new LinkedHashMap<>();

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


        //writing to binary file (.dat) using ObjectOutputStream
//        try (ObjectOutputStream locAndDirFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("locations_big_object_data.dat")))) {
//            for (Location location : locations.values()) {
//                locAndDirFile.writeObject(location);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        //writing to binary file (.dat) using DataOutputStream
//        try (DataOutputStream locAndDirFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("locations_big.dat")))) {
//            for (Location location : locations.values()) {
//                locAndDirFile.writeInt(location.getLocationID());
//                locAndDirFile.writeUTF(location.getDescription());
//                locAndDirFile.writeInt(location.getExits().size());
//                System.out.println("Writing location " + location.getLocationID() + "," + location.getDescription());
//                System.out.println("Writing num of exits : " + (location.getExits().size()));
//                System.out.println("Writing exits:");
//                for (String direction : location.getExits().keySet()) {
//                    System.out.println(direction + " - " + location.getExits().get(direction));
//                    locAndDirFile.writeUTF(direction);
//                    locAndDirFile.writeInt(location.getExits().get(direction));
//                }
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }


        //reading from binary file (.dat) using DataInputStream
//        try (DataInputStream locAndDirFile = new DataInputStream(new BufferedInputStream(new FileInputStream("locations_big.dat")))) {
//            boolean eof = false;
//            while (!eof) {
//                try {
//                    int locID = locAndDirFile.readInt();
//                    String description = locAndDirFile.readUTF();
//                    int exitSize = locAndDirFile.readInt();
//                    locations.put(locID, new Location(locID, description));
//
//                    System.out.println("Location ID : " + locID);
//                    System.out.println("Description : " + description);
//                    System.out.println("Exits size : " + exitSize);
//
//                    for (int i = 0; i < exitSize; i++) {
//                        String direction = locAndDirFile.readUTF();
//                        int destination = locAndDirFile.readInt();
//                        System.out.println(direction + " - " + destination);
//                        locations.get(locID).addExit(direction, destination);
//                    }
//                    System.out.println();
//                } catch (EOFException e) {
//                    eof = true;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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
