package alex.dudchenko.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Vertex implements Comparable<Vertex> {
    private int node;
    private int distance;

    @Override
    public int compareTo(Vertex vertex) {
        return Double.compare(this.distance, vertex.distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return node == vertex.node;
    }

    @Override
    public int hashCode() {
        return Objects.hash(node);
    }
}
