package alex.dudchenko.dijkstra.sequential;

import alex.dudchenko.dijkstra.DijkstraAlgorithm;
import alex.dudchenko.model.Edge;
import alex.dudchenko.model.Graph;
import alex.dudchenko.model.Vertex;
import lombok.Getter;

import java.util.*;

public class DijkstraSequentialAlgorithm implements DijkstraAlgorithm {

    private final Set<Integer> visited;
    private final PriorityQueue<Vertex> queue;
    private final Graph graph;
    private final HashMap<Integer, Integer> distances;
    @Getter
    private final int[] path;

    public DijkstraSequentialAlgorithm(Graph graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
        this.queue = new PriorityQueue<>();
        this.distances = new HashMap<>();

        for (int i = 0; i < graph.getNumberOfNodes(); i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        queue.add(new Vertex(graph.getSourceNode(), 0));
        distances.put(graph.getSourceNode(), 0);

        path = new int[graph.getNumberOfNodes()];
        path[0] = -1;
    }

    @Override
    public List<Integer> solve() {
        while (!queue.isEmpty()) {
            int node = queue.remove().getNode();

            if (!visited.contains(node)) {
                visited.add(node);
                processNeighbours(node);
            }

        }
        return new ArrayList<>(distances.values());
    }

    private void processNeighbours(Integer node) {
        for (Edge edge : graph.getEdgesList()) {
            if (!edge.getFirstNode().equals(node)) continue;

            Integer second = edge.getSecondNode();
            int newDistance = distances.get(node) + edge.getDistance();

            if (newDistance < distances.get(second)) {
                if (!edge.getSecondNode().equals(0)) {
                    path[edge.getSecondNode()] = node;
                }
                distances.put(second, newDistance);
                queue.add(new Vertex(second, newDistance));
            }

        }
    }

    public void showPath(int destinationVertex) {
        if (destinationVertex == -1) {
            return;
        }
        showPath(path[destinationVertex]);
        System.out.print(destinationVertex + " ");
    }
}
