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

        GraphUtils.generateGraphTestMatrix(numberOfNodes);
        Graph graph = IOUtils.readGraph();
        List<Integer> result = Main.runAndMeasureSequentialDijkstra(graph);
        Assert.assertEquals(expected, result.get(result.size() - 1));
    }
}
