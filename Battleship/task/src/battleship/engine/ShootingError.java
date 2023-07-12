package battleship.engine;

public class ShootingError extends RuntimeException {
    ShootingError(String message) {
        super(message);
    }
}
