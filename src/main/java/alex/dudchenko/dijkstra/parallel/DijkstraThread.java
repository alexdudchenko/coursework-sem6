package alex.dudchenko.dijkstra.parallel;

import alex.dudchenko.model.Edge;
import alex.dudchenko.model.Graph;
import alex.dudchenko.model.Vertex;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class DijkstraThread extends Thread {

    private final Graph graph;
    private final int start;
    private final int end;
    private final Set<Integer> visited;
    private final CyclicBarrier cyclicBarrier;
    private final PriorityQueue<Vertex> localQueue;
    private final Map<Integer, Integer> distances;
    private final AtomicBoolean isFinished;
    private final Vertex currentVertex;

    public DijkstraThread(GraphPieceDto dto, CyclicBarrier cyclicBarrier, PriorityQueue<Vertex> localQueue,
                          Map<Integer, Integer> distances, AtomicBoolean isFinished) {
        this.graph = dto.getGraph();
        this.start = dto.getStart();
        this.end = dto.getEnd();
        this.visited = dto.getVisited();
        this.currentVertex = dto.getCurrentVertex();

        this.cyclicBarrier = cyclicBarrier;
        this.localQueue = localQueue;
        this.distances = distances;
        this.isFinished = isFinished;
    }

    @Override
    public void run() {
        while (!isFinished.get()) {
            processNeighbours(currentVertex.getNode());
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private void processNeighbours(Integer node) {
        for (Edge edge : graph.getEdgesList()) {
            if (!edge.getFirstNode().equals(node)) continue;

            Integer second = edge.getSecondNode();
            if (!visited.contains(second)) {
                int newDistance = distances.get(node) + edge.getDistance();

                if (newDistance < distances.get(second)) {
                    distances.put(second, newDistance);
                }
            }
        }
    }
}
