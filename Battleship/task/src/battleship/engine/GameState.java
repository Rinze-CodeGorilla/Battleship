package battleship.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameState {
    IPlayer player;
    IPlayer opponent;

    private final Map<IPlayer, PlayerState> playerStates = new HashMap<>();
    private final ArrayList<IPlayer> players = new ArrayList<>();
    private int index = 0;
    final int WIDTH = 10;
    final int HEIGHT = 10;

    private boolean isFinished = false;

    //TODO: provide an iterator for all the locations on the board?
    private final Ship[] ships = {
            new Ship("Aircraft Carrier", 5),
            new Ship("Battleship", 4),
            new Ship("Submarine", 3),
            new Ship("Cruiser", 3),
            new Ship("Destroyer", 2),
    };

    void nextPlayer() {
        player = players.get(index);
        index = (index + 1) % players.size();
        opponent = players.get(index);
    }

    void addPlayer(IPlayer player) {
        players.add(player);
        playerStates.put(player, new PlayerState());
    }

    void place(Ship ship, Placement placement) {
        validatePlaceShip(ship, placement);
        playerStates.get(player).addShip(ship, placement);
    }

    ShotResult shoot(GridLocation target) {
        validateTarget(target);
        var playerState = playerStates.get(player);
        var opponentState = playerStates.get(opponent);
        var result = opponentState.getShot(target);
        playerState.addShot(target, result != ShotResult.MISS);
        if (result == ShotResult.WON) {
            isFinished = true;
        }
        return result;
    }

    Ship[] getShips() {
        return ships;
    }

    boolean isFinished() {
        return isFinished;
    }

    private void validatePlaceShip(Ship ship, Placement placement) {
        var reversed = placement.start().row() > placement.end().row() || placement.start().column() > placement.end().column();
        final var start = reversed ? placement.end() : placement.start();
        final var end = reversed ? placement.start() : placement.end();

        // check locations
        if (start.row() < 0 || start.row() >= HEIGHT) throw new PlacementError("invalid row for start location");
        if (start.column() < 0 || start.column() >= WIDTH)
            throw new PlacementError("invalid column for start location");
        if (end.row() < 0 || end.row() >= HEIGHT) throw new PlacementError("invalid row for end location");
        if (end.column() < 0 || end.column() >= WIDTH) throw new PlacementError("invalid column for end location");
        // check path
        if (start.row() != end.row() && start.column() != end.column())
            throw new PlacementError("can't place ship diagonally");
        // check length
        if (end.row() - start.row() + end.column() - start.column() + 1 != ship.size())
            throw new PlacementError("locations don't match ship size");
        // check near other ships

        var illegalPlaces = GridLocation.range(
                new GridLocation(start.row() - 1, start.column() - 1),
                new GridLocation(end.row() + 1, end.column() + 1)
        ).toList();
        if (playerStates.get(player).hasShipAtAny(illegalPlaces)) {
            throw new PlacementError("Too close to another ship");
        }
    }

    private void validateTarget(GridLocation target) {
        if (target.row() < 0 || target.row() >= HEIGHT) throw new ShootingError("Invalid row");
        if (target.column() < 0 || target.column() >= WIDTH) throw new ShootingError("Invalid column");

    }

    LocationState opponentStateAt(GridLocation location) {
        var state = playerStates.get(player);
        var hasShot = state.hasShotAt(location);
        if (hasShot == null) {
            return LocationState.WATER;
        } else if (hasShot) {
            return LocationState.HIT;
        } else {
            return LocationState.MISS;
        }
    }

    LocationState playerStateAt(GridLocation location) {
        var state = playerStates.get(player);
        var hasShip = state.hasShipAt(location);
        var wasShot = state.wasShotAt(location);
        if (hasShip && wasShot) {
            return LocationState.HIT;
        } else if (hasShip) {
            return LocationState.SHIP;
        } else if (wasShot) {
            return LocationState.MISS;
        } else {
            return LocationState.WATER;
        }
    }

}
