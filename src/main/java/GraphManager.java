// GraphManager.java
package com.Muizzkhan;

import java.util.*;

public class GraphManager {
    private final Map<String, List<String>> graph = new HashMap<>();
    private final Map<String, double[]> positions = new HashMap<>();

    public boolean addNode(String name) {
        if (name == null || name.isEmpty() || graph.containsKey(name)) return false;
        graph.put(name, new ArrayList<>());
        double x = 100 + Math.random() * 400;
        double y = 100 + Math.random() * 300;
        positions.put(name, new double[]{x, y});
        return true;
    }

    public boolean addEdge(String from, String to) {
        if (!graph.containsKey(from) || !graph.containsKey(to)) return false;
        if (!graph.get(from).contains(to)) graph.get(from).add(to);
        if (!graph.get(to).contains(from)) graph.get(to).add(from);
        return true;
    }

    public List<String> bfsPath(String src, String dst) {
        if (!graph.containsKey(src) || !graph.containsKey(dst)) return null;
        Map<String, String> parent = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(src);
        visited.add(src);
        parent.put(src, null);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        if (!parent.containsKey(dst)) return null;
        List<String> path = new ArrayList<>();
        for (String at = dst; at != null; at = parent.get(at)) path.add(at);
        Collections.reverse(path);
        return path;
    }

    public void generateRandomGraph() {
        graph.clear();
        positions.clear();
        int n = 6 + new Random().nextInt(3);
        for (int i = 0; i < n; i++) {
            String name = "" + (char)('A' + i);
            addNode(name);
        }
        List<String> nodes = new ArrayList<>(graph.keySet());
        for (int i = 0; i < nodes.size(); i++) {
            String from = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                String to = nodes.get(j);
                if (Math.random() < 0.4) {
                    addEdge(from, to);
                }
            }
        }
    }

    public Set<String> getAllNodes() {
        return graph.keySet();
    }

    public List<String> getNeighbors(String node) {
        return graph.getOrDefault(node, new ArrayList<>());
    }

    public double[] getPosition(String node) {
        return positions.getOrDefault(node, new double[]{0, 0});
    }
}
