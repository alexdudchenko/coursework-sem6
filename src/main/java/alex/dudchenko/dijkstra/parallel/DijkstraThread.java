package alex.dudchenko.dijkstra.parallel;

import alex.dudchenko.model.Edge;
import alex.dudchenko.model.Graph;
import alex.dudchenko.model.Vertex;


import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class DijkstraThread extends Thread {

    private final Graph graph;
    private final Set<Integer> visited;
    private final CyclicBarrier cyclicBarrier;
    private final PriorityQueue<Vertex> localQueue;
    private final Map<Integer, Integer> distances;
    private final AtomicBoolean isFinished;
    private final Vertex currentVertex;
    private final Deque<Integer> path;
    private final List<Edge> edges;

    public DijkstraThread(GraphPieceDto dto, CyclicBarrier cyclicBarrier, PriorityQueue<Vertex> localQueue,
                          Map<Integer, Integer> distances, AtomicBoolean isFinished, Deque<Integer> path, List<Edge> edges) {
        this.graph = dto.getGraph();
        this.visited = dto.getVisited();
        this.currentVertex = dto.getCurrentVertex();
        this.cyclicBarrier = cyclicBarrier;
        this.localQueue = localQueue;
        this.distances = distances;
        this.isFinished = isFinished;
        this.path = path;
        this.edges = edges;
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
        for (Edge edge : edges) {
            if (!edge.getFirstNode().equals(node)) continue;

            Integer second = edge.getSecondNode();
            if (!visited.contains(second)) {
                int newDistance = distances.get(node) + edge.getDistance();

                if (newDistance < distances.get(second)) {
                    if (!path.contains(node)) path.push(node);
                    distances.put(second, newDistance);
                    localQueue.add(new Vertex(second, newDistance));
                }
            }
        }
    }
}