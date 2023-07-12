package battleship.engine;

import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * The game class manages the game state through an api for the current player
 * it provides place and shoot functionality
 *
 */
public class GameAPI {

    private final GameState game;

    public GameAPI(GameState game) {
        this.game = game;
    }

    public void placeShip(Ship ship, Placement placement) {
        game.place(ship, placement);
    }

    public ShotResult shoot(GridLocation target) {
        return game.shoot(target);
    }

    public int getWidth() {
        return game.WIDTH;
    }
    public int getHeight() {
        return game.HEIGHT;
    }

    public LocationState playerStateAt(GridLocation location) {
        return game.playerStateAt(location);
    }

    public LocationState opponentStateAt(GridLocation location) {
        return game.opponentStateAt(location);
    }
}
