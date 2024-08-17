package com.example.programmingassignment;

import java.util.ArrayList;
import java.util.List;

public class Q3aCommunityFriendship {

    public static void main(String[] args) {
        int n = 5;
        int[][] restrictions = {{0, 1}, {1, 2}, {2, 3}};
        int[][] requests = {{0, 4}, {1, 2}, {3, 1}, {3, 4}};
        List<String> result = checkFriendRequests(n, restrictions, requests);
        System.out.println(result); // Output: [approved, denied, approved, denied]
    }

    public static List<String> checkFriendRequests(int n, int[][] restrictions, int[][] requests) {
        UnionFind uf = new UnionFind(n);
        List<String> results = new ArrayList<>();

        // Process each friend request
        for (int[] request : requests) {
            int u = request[0];
            int v = request[1];

            // Check if the request would violate any restriction
            boolean canBeFriends = true;
            for (int[] restriction : restrictions) {
                if ((uf.find(u) == uf.find(restriction[0]) && uf.find(v) == uf.find(restriction[1])) ||
                        (uf.find(u) == uf.find(restriction[1]) && uf.find(v) == uf.find(restriction[0]))) {
                    canBeFriends = false;
                    break;
                }
            }

            // If no restriction is violated, approve the request
            if (canBeFriends) {
                uf.union(u, v);
                results.add("approved");
            } else {
                results.add("denied");
            }
        }

        return results;
    }
}

class UnionFind {
    private final int[] parent;
    private final int[] rank;

    // Initialize Union-Find structure
    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    // Find with path compression
    public int find(int p) {
        if (p != parent[p]) {
            parent[p] = find(parent[p]);
        }
        return parent[p];
    }

    // Union by rank
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP != rootQ) {
            if (rank[rootP] > rank[rootQ]) {
                parent[rootQ] = rootP;
            } else if (rank[rootP] < rank[rootQ]) {
                parent[rootP] = rootQ;
            } else {
                parent[rootQ] = rootP;
                rank[rootP]++;
            }
        }
    }
}
