package alex.dudchenko.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Graph {
    private final int sourceNode;
    private final int numberOfNodes;
    private final List<Edge> edgesList;

    public Graph(int sourceNode, int numberOfNodes, List<Edge> edgesList) {
        this.sourceNode = sourceNode;
        this.numberOfNodes = numberOfNodes;
        this.edgesList = edgesList;
    }
}
