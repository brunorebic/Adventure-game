package com.BrunoRebic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Locations locations = new Locations();

    public static void main(String[] args) {
//        Locations.printLocationsAndExits();//optional - print all locations and exists

        Scanner scanner = new Scanner(System.in);

        int loc = 1;
        while (true) {
            System.out.println(locations.get(loc).getDescription());
            if (loc == 0)
                break;

            Map<String, Integer> exits = locations.get(loc).getExits();
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
            if (exits.containsKey(direction))
                loc = exits.get(direction);
            else
                System.out.println("You cannot go in that direction!");
        }
    }
}


