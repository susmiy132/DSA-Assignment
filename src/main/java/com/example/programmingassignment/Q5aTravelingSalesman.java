package com.example.programmingassignment;
import lombok.Getter;

import java.util.Arrays;
import java.util.Random;

public class Q5aTravelingSalesman {

    private final int[][] distanceMatrix;
    private final int numberOfCities;
    private int[] bestTour;
    @Getter
    private int bestTourLength;

    private Q5aTravelingSalesman(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.numberOfCities = distanceMatrix.length;
        this.bestTour = new int[numberOfCities];
        this.bestTourLength = Integer.MAX_VALUE;
    }

    private void solve() {
        // Generate an initial random tour
        int[] currentTour = generateRandomTour();
        bestTour = Arrays.copyOf(currentTour, currentTour.length);
        bestTourLength = calculateTourLength(currentTour);

        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 1; i < numberOfCities - 1; i++) {
                for (int j = i + 1; j < numberOfCities; j++) {
                    // Swap cities i and j
                    swap(currentTour, i, j);
                    int currentTourLength = calculateTourLength(currentTour);
                    // If the new tour is better, update the best tour
                    if (currentTourLength < bestTourLength) {
                        bestTourLength = currentTourLength;
                        bestTour = Arrays.copyOf(currentTour, currentTour.length);
                        improvement = true;
                    } else {
                        // Swap back if not better
                        swap(currentTour, i, j);
                    }
                }
            }
        }
    }

    private int[] generateRandomTour() {
        int[] tour = new int[numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            tour[i] = i;
        }
        shuffleArray(tour);
        return tour;
    }

    private void shuffleArray(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            // Swap
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private int calculateTourLength(int[] tour) {
        int length = 0;
        for (int i = 0; i < tour.length; i++) {
            int fromCity = tour[i];
            int toCity = tour[(i + 1) % tour.length]; // Wrap around to the start
            length += distanceMatrix[fromCity][toCity];
        }
        return length;
    }

    private void swap(int[] tour, int i, int j) {
        int temp = tour[i];
        tour[i] = tour[j];
        tour[j] = temp;
    }

    private int[] getBestTour() {
        return bestTour;
    }

    public static void main(String[] args) {
        // Example distance matrix
        int[][] distanceMatrix = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        Q5aTravelingSalesman tsp = new Q5aTravelingSalesman(distanceMatrix);
        tsp.solve();

        System.out.println("Best tour: " + Arrays.toString(tsp.getBestTour()));
        System.out.println("Best tour length: " + tsp.getBestTourLength());
    }
}