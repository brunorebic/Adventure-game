package com.BrunoRebic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Locations locations = new Locations();

    public static void main(String[] args) throws IOException {
//        Locations.printLocationsAndExits();//optional - print all locations and exists
        System.out.println("Adventure game :\n");

        Scanner scanner = new Scanner(System.in);

        Location currentLocation = locations.getLocation(1);
        while (true) {
            System.out.println(currentLocation.getDescription());
            if (currentLocation.getLocationID() == 0)
                break;

            Map<String, Integer> exits = currentLocation.getExits();
            Iterator<String> it = exits.keySet().iterator();

            System.out.print("Available exits are : ");
            while (it.hasNext()) {
                String str = it.next();
                if (!it.hasNext()) {
                    System.out.print(str);
                    break;
                }
                System.out.print(str + ", ");
            }
            System.out.println();
            System.out.println("Enter choice:");
            String direction = scanner.nextLine().toUpperCase();

            Map<String, String> map = new HashMap<>();
            map.put("QUIT", "Q");
            map.put("EAST", "E");
            map.put("WEST", "W");
            map.put("SOUTH", "S");
            map.put("NORTH", "N");
            map.put("DOWN", "D");
            map.put("UP", "U");

            if (direction.length() > 1) {
                String[] directionArray = direction.split("[ ,.!?]");
                for (String dir : directionArray) {
                    if (map.containsKey(dir))
                        direction = map.get(dir);
                }
            }
            if (exits.containsKey(direction)) {
                int id = currentLocation.getExits().get(direction);
                currentLocation = locations.getLocation(id);
            } else
                System.out.println("You cannot go in that direction!");
        }
        locations.close();
    }
}


