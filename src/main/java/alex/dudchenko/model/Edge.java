package alex.dudchenko.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Edge {

    private final Integer firstNode;
    private final Integer secondNode;
    private final Integer distance;
}
