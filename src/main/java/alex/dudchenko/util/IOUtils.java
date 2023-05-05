package alex.dudchenko.util;

import alex.dudchenko.model.Graph;
import alex.dudchenko.exception.InvalidInputDataException;
import alex.dudchenko.model.Vertex;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IOUtils {
    public static final String SPACE_SEPARATOR = " ";
    private static final String FILE_NAME = "graph.txt";
    private static final String OUT_FILE = "distances.txt";
    private static final String INF_STRING = "Inf";

    private IOUtils() {
    }

    public static void writeResults(List<Integer> results) throws InvalidInputDataException {
        File file = new File(OUT_FILE);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath())) {
            for (Integer result : results) {
                String line;
                if (result < Integer.MAX_VALUE) {
                    line = String.valueOf(result);
                } else {
                    line = INF_STRING;
                }
                bufferedWriter.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new InvalidInputDataException(e.getMessage());
        }
    }

    public static Graph readGraph(String filename) throws InvalidInputDataException {
        List<Map<Integer, Vertex>> edges = new ArrayList<>();
        int source;
        int numberOfNodes;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line = bufferedReader.readLine();
            if (line != null) {
                String[] params = line.split(SPACE_SEPARATOR);
                numberOfNodes = Integer.parseInt(params[0]);
                source = Integer.parseInt(params[1]);
            } else {
                throw new InvalidInputDataException("Provide the number of vertices");
            }
            while ((line = bufferedReader.readLine()) != null) {
                Map<Integer, Vertex> row = new HashMap<>(numberOfNodes);
                String[] params = line.split(SPACE_SEPARATOR);
                for (int i = 0; i < params.length; i++) {
                    int val = Integer.parseInt(params[i]);
                    if (val > 0) {
                        row.put(i, new Vertex(i, val));
                    }
                }
                edges.add(row);
            }
        } catch (IOException e) {
            throw new InvalidInputDataException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new InvalidInputDataException("Vertices and weights are Integers");
        }
        return new Graph(source, edges);
    }

    public static Graph readGraph() throws InvalidInputDataException {
        return readGraph(FILE_NAME);
    }
}
