package com.example.programmingassignment;

public class Q5bLongestHike {

    /**
     * Finds the longest consecutive hike where the elevation gain between consecutive points
     * does not exceed the given limit k.
     *
     * @param trail An array of integers representing the altitudes on the trail.
     * @param k The maximum allowed elevation gain between two consecutive points.
     * @return The length of the longest consecutive hike that respects the elevation gain limit.
     */
    public static int findLongestHike(int[] trail, int k) {
        int n = trail.length;
        if (n == 0) {
            return 0;
        }

        int maxLength = 0;
        int start = 0;

        // Traverse the trail array with the end pointer
        for (int end = 1; end < n; end++) {
            // Expand the window while the elevation gain between consecutive points is within the limit
            while (end < n && trail[end] - trail[end - 1] <= k) {
                end++;
            }

            // Update the maximum length of the valid window
            maxLength = Math.max(maxLength, end - start);

            // Move the start pointer to the right to find the next valid window
            while (end < n && trail[end] - trail[end - 1] > k) {
                start = end;
                end++;
            }
        }

        return maxLength;
    }

    public static void main(String[] args) {
        int[] trail = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k = 3;
        int result = findLongestHike(trail, k);
        System.out.println("Longest hike length: " + result);  // Expected Output: 5
    }
}
