package alex.dudchenko.dijkstra.parallel;

import alex.dudchenko.model.Graph;
import alex.dudchenko.model.Vertex;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GraphPieceDto {
    private Graph graph;
    private int start;
    private int end;
    private Set<Integer> visited;
    private Vertex currentVertex;
}
