package alex.dudchenko.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Graph {
    private final int sourceNode;
    private final int numberOfNodes;
    private final List<Map<Integer, Vertex>> edges;
    private final List<Edge> edgesList;

    public Graph(int sourceNode, List<Map<Integer, Vertex>> edges, List<Edge> edgesList) {
        this.sourceNode = sourceNode;
        this.numberOfNodes = edges.size();
        this.edges = edges;
        this.edgesList = edgesList;
    }
}
