package battleship.engine;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record GridLocation(int row, int column) {
    public GridLocation(String location) {
        this(
                location.toUpperCase().charAt(0) - 'A',
                Integer.parseInt(location.substring(1)) - 1
        );
    }

    static Stream<GridLocation> range(GridLocation start, GridLocation end) {
        var rowStream = IntStream.rangeClosed(Math.min(start.row(), end.row()), Math.max(start.row(), end.row()));
        var gridLocationStream = rowStream.mapToObj(y -> IntStream.rangeClosed(Math.min(start.column(), end.column()), Math.max(start.column(), end.column())).mapToObj(x -> new GridLocation(y, x)));
        return gridLocationStream.flatMap(Function.identity());
    }
}
