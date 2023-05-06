package alex.dudchenko;

import alex.dudchenko.dijkstra.parallel.DijkstraParallelAlgorithm;
import alex.dudchenko.dijkstra.sequential.DijkstraSequentialAlgorithm;
import alex.dudchenko.exception.InvalidInputDataException;
import alex.dudchenko.model.Graph;
import alex.dudchenko.util.GraphUtils;
import alex.dudchenko.util.IOUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

public class Main {

    private static final String SEQ_OPTION = "seq";
    private static final String PAR_OPTION = "par";

    public static void main(String[] args) {
        Graph graph;
        List<Integer> results;

        try {
            if (args.length == 3 && SEQ_OPTION.equals(args[0])) {
                GraphUtils.generateGraphMatrix(Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]));
                graph = IOUtils.readGraph();
                results = runAndMeasureSequentialDijkstra(graph);
            } else if (args.length == 4 && PAR_OPTION.equals(args[0])) {
                GraphUtils.generateGraphMatrix(Integer.parseInt(args[2]),
                        Integer.parseInt(args[3]));
                graph = IOUtils.readGraph();
                results = runAndMeasureParallelDijkstra(graph, Integer.parseInt(args[1]));
            } else if (args.length == 1 && SEQ_OPTION.equals(args[0])) {
                graph = IOUtils.readGraph();
                results = runAndMeasureSequentialDijkstra(graph);
            } else if (args.length == 2 && PAR_OPTION.equals(args[0])) {
                graph = IOUtils.readGraph();
                results = runAndMeasureParallelDijkstra(graph, Integer.parseInt(args[1]));
            } else {
                throw new InvalidInputDataException("Provide right options!");
            }
            IOUtils.writeResults(results);
        } catch (InvalidInputDataException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Integer> runAndMeasureSequentialDijkstra(Graph graph) {
        DijkstraSequentialAlgorithm dijkstraSequential = new DijkstraSequentialAlgorithm(graph);
        Instant start = Instant.now();
        List<Integer> results = dijkstraSequential.solve();
        Instant end = Instant.now();
        long time = Duration.between(start, end).toMillis();
        System.out.println("Total time, millis = " + time);
        System.out.print("Path: ");
        Iterator<Integer> iterator = dijkstraSequential.getPath().descendingIterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        return results;
    }

    public static List<Integer> runAndMeasureParallelDijkstra(Graph graph, int numberOfThreads) {
        DijkstraParallelAlgorithm dijkstraParallel = new DijkstraParallelAlgorithm(graph, numberOfThreads);
        Instant start = Instant.now();
        List<Integer> results = dijkstraParallel.solve();
        Instant end = Instant.now();
        long time = Duration.between(start, end).toMillis();
        System.out.println("Total time, millis = " + time);
        System.out.print("Path: ");
        Iterator<Integer> iterator = dijkstraParallel.getPath().descendingIterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        return results;
    }
}
