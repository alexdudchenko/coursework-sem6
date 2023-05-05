package alex.dudchenko.dijkstra;

import alex.dudchenko.Main;
import alex.dudchenko.model.Graph;
import alex.dudchenko.util.GraphUtils;
import alex.dudchenko.util.IOUtils;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SequentialDijkstraAlgorithmTest {

    private static final int numberOfNodes = 1000;

    @SneakyThrows
    @Test
    public void generateTestMatrixAndSolve() {
        Integer expected = numberOfNodes - 1;

        Graph graph = IOUtils.readGraph("test.txt");
        List<Integer> result = Main.runAndMeasureSequentialDijkstra(graph);

        Integer distance = result.get(result.size() - 1);
        System.out.println("Distance from source vertex to finish: " + distance);

        Assert.assertEquals(expected, distance);
    }
}
