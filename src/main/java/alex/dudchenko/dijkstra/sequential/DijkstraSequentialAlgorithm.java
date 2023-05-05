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
    private final Queue<Integer> path = new ArrayDeque<>();

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
            path.add(node);
            if (!visited.contains(node)) {
                visited.add(node);
                processNeighbours(node);
            }
        }
        return new ArrayList<>(distances.values());
    }

    private void processNeighbours(int node) {
        Map<Integer, Vertex> neighbours = graph.getEdges().get(node);

        for (int i = 0; i < graph.getNumberOfNodes(); i++) {
            if (neighbours.containsKey(i) && !visited.contains(i)) {
                int newDistance = distances.get(node) + neighbours.get(i).getDistance();
                if (newDistance < distances.get(i)) {
                    distances.put(i, newDistance);
                    queue.add(new Vertex(i, newDistance));
                }
            }
        }
    }
}
