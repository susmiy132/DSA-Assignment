package com.example.programmingassignment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ClassroomScheduler {

    // Class to represent a class with its start time, end time, and size
    static class Class {
        int start;
        int end;
        int size;

        // Constructor
        public Class(int start, int end) {
            this.start = start;
            this.end = end;
            this.size = end - start; // Assuming size is the duration of the class
        }
    }

    public static int mostClasses(int n, int[][] classesArr) {
        // Convert input array to a list of Class objects
        List<Class> classes = new ArrayList<>();
        for (int[] cls : classesArr) {
            classes.add(new Class(cls[0], cls[1]));
        }

        // Sort classes by start time and then by size (duration)
        classes.sort((a, b) -> {
            if (a.start != b.start) {
                return Integer.compare(a.start, b.start);
            } else {
                return Integer.compare(b.size, a.size); // Larger class first if start time is the same
            }
        });

        // Priority queue (min-heap) to manage when rooms will be free
        PriorityQueue<int[]> freeRooms = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int[] roomUsageCount = new int[n]; // Array to count the usage of each room
        int roomIndex = 0; // To allocate rooms initially

        for (Class cls : classes) {

            // Check if any room is free at cls.start
            while (!freeRooms.isEmpty() && freeRooms.peek()[0] <= cls.start) {
                int[] room = freeRooms.poll();

                // Room becomes available again
                room[0] += (cls.end - cls.start); // Increment when room will be occupied
                roomUsageCount[room[1]]++; // Increment the count of classes held in the room
                freeRooms.offer(room); // Put it back to the priority queue
            }

            // Allocate a room
            if (roomIndex < n) {

                // If there are free rooms (not yet allocated)
                freeRooms.offer(new int[]{cls.end, roomIndex++}); // [when will free up, room number]
                roomUsageCount[roomIndex - 1]++; // Increment usage
            } else {

                // All rooms occupied - delay the class until a room is free
                if (!freeRooms.isEmpty()) {
                    int[] room = freeRooms.poll();

                    // Class starts when the room is free
                    cls.start = room[0];
                    cls.end = cls.start + (cls.end - cls.start); // Retain duration
                    freeRooms.offer(new int[]{cls.end, room[1]}); // Offer the room back
                    roomUsageCount[room[1]]++; // Increment usage
                }
            }
        }

        // Determine which room has the maximum number of classes
        int maxClasses = -1;
        int bestRoom = -1;
        for (int i = 0; i < n; i++) {
            if (roomUsageCount[i] > maxClasses) {
                maxClasses = roomUsageCount[i];
                bestRoom = i;
            } else if (roomUsageCount[i] == maxClasses && bestRoom > i) {
                bestRoom = i; // Prefer room with a lower number in case of tie
            }
        }

        return bestRoom; // Return the room with the most classes held
    }

    public static void main(String[] args) {
        int n = 2;
        int[][] classes = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
        System.out.println(mostClasses(n, classes)); // Should print 0

        n = 3;
        classes = new int[][]{{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}};
        System.out.println(mostClasses(n, classes)); // Should print 1
    }
}
