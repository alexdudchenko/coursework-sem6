package alex.dudchenko.dijkstra.parallel;

import alex.dudchenko.dijkstra.DijkstraAlgorithm;
import alex.dudchenko.exception.InterruptedRuntimeException;
import alex.dudchenko.model.Graph;
import alex.dudchenko.model.Vertex;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class DijkstraParallelAlgorithm implements DijkstraAlgorithm {

    private final Graph graph;
    private final Set<Integer> visited;
    private final List<PriorityQueue<Vertex>> queues;
    private final Map<Integer, Integer> distances;
    private final AtomicBoolean isFinished;
    private final Vertex currentVertex;
    private final int numberOfThreads;

    public DijkstraParallelAlgorithm(Graph graph, int numberOfThreads) {
        this.graph = graph;
        this.numberOfThreads = numberOfThreads;
        this.distances = new HashMap<>();
        this.visited = new HashSet<>();
        this.queues = new ArrayList<>();

        for (int i = 0; i < graph.getNumberOfNodes(); i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(graph.getSourceNode(), 0);
        isFinished = new AtomicBoolean(false);
        currentVertex = new Vertex(graph.getSourceNode(), 0);
    }

    @Override
    public List<Integer> solve() {
        for (int i = 0; i < numberOfThreads; i++) {
            PriorityQueue<Vertex> localQueue = new PriorityQueue<>();
            queues.add(localQueue);
        }
        List<Thread> threads = breakIntoTasks();

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new InterruptedRuntimeException(e.getMessage());
            }
        }
        return new LinkedList<>(distances.values());
    }

    private List<Thread> breakIntoTasks() {
        List<Thread> threads = new ArrayList<>();

        ReduceOperationRunnable reduceOperationRunnable = new ReduceOperationRunnable(queues, isFinished, visited, currentVertex);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfThreads, reduceOperationRunnable);
        int start;
        int end = 0;
        int chunk = graph.getNumberOfNodes() / numberOfThreads;
        int leftover = graph.getNumberOfNodes() % numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            start = end;
            end += chunk;
            if (leftover > 0) {
                end++;
                leftover--;
            }

            GraphPieceDto dto = new GraphPieceDto();
            dto.setGraph(graph);
            dto.setStart(start);
            dto.setEnd(end);
            dto.setVisited(visited);
            dto.setCurrentVertex(currentVertex);

            Thread dijThread = new DijkstraThread(dto, cyclicBarrier, queues.get(i), distances, isFinished);
            threads.add(dijThread);
        }
        return threads;
    }

    public static class ReduceOperationRunnable implements Runnable {

        private final List<PriorityQueue<Vertex>> queues;
        private final AtomicBoolean isFinished;
        private final Set<Integer> visited;
        private final Vertex currentVertex;

        public ReduceOperationRunnable(List<PriorityQueue<Vertex>> queues, AtomicBoolean isFinished, Set<Integer> visited, Vertex currentVertex) {
            this.queues = queues;
            this.isFinished = isFinished;
            this.visited = visited;
            this.currentVertex = currentVertex;
        }

        @Override
        public void run() {
            while (true) {
                Vertex minVertex = null;
                int queueIndex = 0;
                for (int i = 0; i < queues.size(); i++) {
                    if (!queues.get(i).isEmpty()) {
                        Vertex vertex = queues.get(i).peek();
                        if (minVertex == null ||
                                Objects.requireNonNull(vertex).compareTo(minVertex) < 0) {
                            minVertex = vertex;
                            queueIndex = i;
                        }
                    }
                }
                if (minVertex == null) {
                    isFinished.set(true);
                    return;
                } else if (!visited.contains(minVertex.getNode())) {
                    visited.add(minVertex.getNode());
                    currentVertex.setNode(minVertex.getNode());
                    currentVertex.setDistance(minVertex.getDistance());
                    queues.get(queueIndex).remove();
                    return;
                } else {
                    queues.get(queueIndex).remove();
                }
            }
        }
    }
}
