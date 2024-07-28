package com.example.programmingassignment;

import java.util.ArrayList;
import java.util.List;

public class Q3aCommunityFriendship {

    public static void main(String[] args) {
        int n = 5;
        int[][] restrictions = {{0, 1}, {1, 2}, {2, 3}};
        int[][] requests = {{0, 4}, {1, 2}, {3, 1}, {3, 4}};
        List<String> result = checkFriendRequests(n, restrictions, requests);
        System.out.println(result); // Output should be [approved, denied, approved, denied]
    }

    public static List<String> checkFriendRequests(int n, int[][] restrictions, int[][] requests) {
        UnionFind uf = new UnionFind(n);
        List<String> results = new ArrayList<>();

        for (int[] request : requests) {
            int u = request[0];
            int v = request[1];
            int rootU = uf.find(u);
            int rootV = uf.find(v);

            boolean canBeFriends = true;
            for (int[] restriction : restrictions) {
                int x = restriction[0];
                int y = restriction[1];
                int rootX = uf.find(x);
                int rootY = uf.find(y);

                // If the requested union (rootU, rootV) would violate a restriction (rootX, rootY), deny the request
                if ((rootU == rootX && rootV == rootY) || (rootU == rootY && rootV == rootX)) {
                    canBeFriends = false;
                    break;
                }
            }

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
    int[] parent;
    int[] rank;

    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    public int find(int p) {
        if (p != parent[p]) {
            parent[p] = find(parent[p]); // path compression
        }
        return parent[p];
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);

        if (rootP != rootQ) {
            // union by rank
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
