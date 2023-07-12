package battleship.engine;

public class PlacementError extends RuntimeException {
    PlacementError(String message) {
        super(message);
    }
}
