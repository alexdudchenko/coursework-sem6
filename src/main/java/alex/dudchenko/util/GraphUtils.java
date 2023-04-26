package alex.dudchenko.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class GraphUtils {

    private static final String FILE_NAME = "graph.txt";
    private static final Random random = new Random();
    private static final String ZERO = "0";
    private static final String NO_CONNECTION = "-1";

    private GraphUtils() {
    }

    public static void generateGraphMatrix(int numberOfNodes, int density) throws IOException {

        File file = new File(FILE_NAME);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath())) {
            bufferedWriter.append(String.valueOf(numberOfNodes)).append(IOUtils.SPACE_SEPARATOR)
                    .append(String.valueOf(0)).append(System.lineSeparator());

            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    if (i == j) {
                        bufferedWriter.append(ZERO).append(IOUtils.SPACE_SEPARATOR);
                    } else {
                        int r = random.nextInt(99) + 1;
                        if (r <= density) {
                            int val = random.nextInt(1000);
                            bufferedWriter.append(String.valueOf(val)).append(IOUtils.SPACE_SEPARATOR);
                        } else {
                            bufferedWriter.append(NO_CONNECTION).append(IOUtils.SPACE_SEPARATOR);
                        }
                    }
                }
                bufferedWriter.append(System.lineSeparator());
            }
        }
    }

    public static void generateGraphTestMatrix(int numberOfNodes) throws IOException {
        File file = new File(FILE_NAME);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath())) {
            bufferedWriter.append(String.valueOf(numberOfNodes)).append(IOUtils.SPACE_SEPARATOR)
                    .append(String.valueOf(0)).append(System.lineSeparator());

            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    if (i == j) {
                        bufferedWriter.append(ZERO).append(IOUtils.SPACE_SEPARATOR);
                    } else {
                        if (j - i == 1) {
                            bufferedWriter.append(String.valueOf(1)).append(IOUtils.SPACE_SEPARATOR);
                        } else {
                            bufferedWriter.append(String.valueOf(9999)).append(IOUtils.SPACE_SEPARATOR);
                        }
                    }
                }
                bufferedWriter.append(System.lineSeparator());
            }
        }
    }
}
