package alex.dudchenko.dijkstra.parallel;

import alex.dudchenko.model.Graph;
import alex.dudchenko.model.Vertex;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class DijkstraThread extends Thread {

    private final Graph graph;
    private final int start;
    private final int end;
    private final CyclicBarrier cyclicBarrier;
    private final PriorityQueue<Vertex> localQueue;
    private final Map<Integer, Integer> distances;
    private final AtomicBoolean isFinished;
    private final Vertex currentVertex;
    private final int[] path;

    public DijkstraThread(GraphPieceDto dto, CyclicBarrier cyclicBarrier, PriorityQueue<Vertex> localQueue,
                          Map<Integer, Integer> distances, AtomicBoolean isFinished, int[] path) {
        this.graph = dto.getGraph();
        this.start = dto.getStart();
        this.end = dto.getEnd();
        this.currentVertex = dto.getCurrentVertex();

        this.cyclicBarrier = cyclicBarrier;
        this.localQueue = localQueue;
        this.distances = distances;
        this.isFinished = isFinished;
        this.path = path;
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

    private void processNeighbours(int node) {
        Map<Integer, Vertex> neighbours = graph.getEdges().get(node);

        for (int i = start; i < end; i++) {
            if (neighbours.containsKey(i)) {
                int newDistance = distances.get(node) + neighbours.get(i).getDistance();
                if (newDistance < distances.get(neighbours.get(i).getNode())) {
                    if (i != 0) {
                        path[i] = node;
                    }

                    distances.put(neighbours.get(i).getNode(), newDistance);
                    localQueue.add(new Vertex(i, newDistance));
                }
            }
        }
    }
}
