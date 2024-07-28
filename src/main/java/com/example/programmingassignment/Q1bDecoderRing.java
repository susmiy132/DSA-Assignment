package com.example.programmingassignment;

public class Q1bDecoderRing {

    public static void main(String[] args) {
        String s = "Beauty";
        int[][] shifts = {{0, 1, 1}, {2, 3, 0}, {0, 2, 1}};
        String result = decipherMessage(s, shifts);
        System.out.println(result); // Output should be "^gatty"
    }

    public static String decipherMessage(String s, int[][] shifts) {
        char[] message = s.toCharArray();

        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int direction = shift[2];

            for (int i = start; i <= end; i++) {
                if (direction == 1) {
                    // Rotate clockwise
                    message[i] = (char) ((message[i] - 'a' + 1) % 26 + 'a');
                } else {
                    // Rotate counter-clockwise
                    message[i] = (char) ((message[i] - 'a' - 1 + 26) % 26 + 'a');
                }
            }
        }

        return new String(message);
    }
}

