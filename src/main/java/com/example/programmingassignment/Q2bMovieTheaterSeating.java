package com.example.programmingassignment;


import java.util.TreeSet;

public class Q2bMovieTheaterSeating {

    public static void main(String[] args) {
        int[] nums = {2, 3, 5, 4, 9};
        int indexDiff = 2;
        int valueDiff = 1;
        boolean result = canSitTogether(nums, indexDiff, valueDiff);
        System.out.println(result); // Output should be true
    }

    public static boolean canSitTogether(int[] nums, int indexDiff, int valueDiff) {
        // A TreeSet to keep track of the recent `indexDiff` elements
        TreeSet<Integer> set = new TreeSet<>();

        for (int i = 0; i < nums.length; i++) {
            // Check the smallest number >= nums[i] - valueDiff
            Integer floor = set.ceiling(nums[i] - valueDiff);
            // Check the largest number <= nums[i] + valueDiff
            Integer ceiling = set.floor(nums[i] + valueDiff);

            // If either exists and satisfies the movie preference condition, return true
            if ((floor != null && Math.abs(floor - nums[i]) <= valueDiff) ||
                    (ceiling != null && Math.abs(ceiling - nums[i]) <= valueDiff)) {
                return true;
            }

            // Add the current number to the set
            set.add(nums[i]);

            // Maintain the size of the TreeSet to indexDiff
            if (set.size() > indexDiff) {
                set.remove(nums[i - indexDiff]);
            }
        }
        return false;
    }
}
