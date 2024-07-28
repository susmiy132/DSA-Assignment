package com.example.programmingassignment;

import java.util.*;

public class Q4aCityPlanner {

    public static void main(String[] args) {
        int n = 5;
        int[][] roads = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};
        int source = 0;
        int destination = 1;
        int target = 5;

        int[][] result = modifyRoadNetwork(n, roads, source, destination, target);
        for (int[] road : result) {
            System.out.println(Arrays.toString(road));
        }
    }

    private static int[][] modifyRoadNetwork(int n, int[][] roads, int source, int destination, int target) {
        // Initialize the graph with given roads and a map to track roads under construction
        List<int[]>[] graph = new ArrayList[n];
        Map<int[], Integer> underConstruction = new HashMap<>();

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            int w = road[2];
            graph[u].add(new int[]{v, w});
            graph[v].add(new int[]{u, w});

            if (w == -1) {
                underConstruction.put(road, 0); // Use 0 as a placeholder for initial weights
            }
        }

        // Binary search over the possible range of weights for the roads under construction
        int low = 1;
        int high = 2 * 10^9;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            // Set all under construction roads to the mid-value
            underConstruction.replaceAll((r, v) -> mid);

            // Update the graph with the new weights
            for (Map.Entry<int[], Integer> entry : underConstruction.entrySet()) {
                int[] road = entry.getKey();
                int weight = entry.getValue();
                graph[road[0]].add(new int[]{road[1], weight});
                graph[road[1]].add(new int[]{road[0], weight});
            }

            int shortestPath = dijkstra(graph, source, destination);

            if (shortestPath == target) {
                // Found the correct weights, update the roads array
                for (int[] road : roads) {
                    if (underConstruction.containsKey(road)) {
                        road[2] = underConstruction.get(road);
                    }
                }
                return roads;
            } else if (shortestPath < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return new int[][]{{-1}};
    }

    private static int dijkstra(List<int[]>[] graph, int source, int destination) {
        int n = graph.length;
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int d = current[1];

            if (u == destination) {
                return d;
            }

            for (int[] neighbor : graph[u]) {
                int v = neighbor[0];
                int weight = neighbor[1];

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }

        return dist[destination];
    }
}

