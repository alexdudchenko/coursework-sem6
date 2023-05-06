package alex.dudchenko.dijkstra.sequential;

import alex.dudchenko.dijkstra.DijkstraAlgorithm;
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
    private final Deque<Integer> path = new ArrayDeque<>();

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
        if (!path.contains(graph.getNumberOfNodes() - 1)) path.push(graph.getNumberOfNodes() - 1);
        return new ArrayList<>(distances.values());
    }

    private void processNeighbours(int node) {
        Map<Integer, Vertex> neighbours = graph.getEdges().get(node);

        for (int i = 0; i < graph.getNumberOfNodes(); i++) {
            if (neighbours.containsKey(i)) {
                int newDistance = distances.get(node) + neighbours.get(i).getDistance();
                if (newDistance < distances.get(i)) {
                    if (!path.contains(node)) path.push(node);
                    distances.put(i, newDistance);
                    queue.add(new Vertex(i, newDistance));
                }
            }
        }
    }
}
