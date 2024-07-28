package com.example.programmingassignment;


class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int x) { val = x; }
}

public class Q4bTreasureHunt {

    public static void main(String[] args) {
        // Construct the example tree
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.left = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        int maxSum = largestBSTSubtreeSum(root);
        System.out.println(maxSum); // Output should be 20
    }

    static class BSTInfo {
        boolean isBST;
        int sum;
        int min;
        int max;

        BSTInfo(boolean isBST, int sum, int min, int max) {
            this.isBST = isBST;
            this.sum = sum;
            this.min = min;
            this.max = max;
        }
    }

    public static int largestBSTSubtreeSum(TreeNode root) {
        int[] maxSum = new int[1];
        maxSum[0] = 0;
        postOrder(root, maxSum);
        return maxSum[0];
    }

    private static BSTInfo postOrder(TreeNode node, int[] maxSum) {
        if (node == null) {
            return new BSTInfo(true, 0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        BSTInfo leftInfo = postOrder(node.left, maxSum);
        BSTInfo rightInfo = postOrder(node.right, maxSum);

        if (leftInfo.isBST && rightInfo.isBST && node.val > leftInfo.max && node.val < rightInfo.min) {
            int sum = node.val + leftInfo.sum + rightInfo.sum;
            maxSum[0] = Math.max(maxSum[0], sum);
            int minValue = node.left == null ? node.val : leftInfo.min;
            int maxValue = node.right == null ? node.val : rightInfo.max;
            return new BSTInfo(true, sum, minValue, maxValue);
        }

        return new BSTInfo(false, 0, 0, 0);
    }
}
