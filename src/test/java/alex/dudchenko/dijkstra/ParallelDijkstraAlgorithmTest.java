package alex.dudchenko.dijkstra;

import alex.dudchenko.Main;
import alex.dudchenko.model.Graph;
import alex.dudchenko.util.GraphUtils;
import alex.dudchenko.util.IOUtils;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParallelDijkstraAlgorithmTest {

    private static final int numberOfNodes = 1000;
    private static final int numberOfThreads = 2;

    @SneakyThrows
    @Test
    public void generateTestMatrixAndSolve() {
        Integer expected = numberOfNodes - 1;

        Graph graph = IOUtils.readGraph("test1.txt");
        List<Integer> result = Main.runAndMeasureParallelDijkstra(graph, numberOfThreads);

        Integer distance = result.get(result.size() - 1);
        System.out.println("Distance from source vertex to finish: " + distance);
        Assert.assertEquals(expected, distance);
    }

    @SneakyThrows
    @Test
    public void generateTestMatrixAndSolve2() {
        Integer expected = 597;

        Graph graph = IOUtils.readGraph("test2.txt");
        List<Integer> result = Main.runAndMeasureParallelDijkstra(graph, numberOfThreads);

        Integer distance = result.get(result.size() - 1);
        System.out.println("Distance from source vertex to finish: " + distance);
        Assert.assertEquals(expected, distance);
    }
}
