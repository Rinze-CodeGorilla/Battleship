package battleship.engine;

import java.util.*;
import java.util.stream.Collectors;

class PlayerState {
    private final Map<Ship, HashSet<GridLocation>> ships = new HashMap<>();
    private final Map<GridLocation, Boolean> shotsFired = new HashMap<>();
    private final Set<GridLocation> shotsReceived = new HashSet<>();

    boolean hasShipAt(GridLocation location) {
        return ships.entrySet().stream().anyMatch((Map.Entry<Ship, HashSet<GridLocation>> es) -> es.getValue().contains(location));
    }

    private Ship shipAt(GridLocation location) {
        var ship = ships.entrySet().stream().filter((Map.Entry<Ship, HashSet<GridLocation>> e) -> e.getValue().contains(location)).findAny();
        return ship.map(Map.Entry::getKey).orElse(null);
    }

    boolean hasShipAtAny(List<GridLocation> list) {
        return ships.values().stream().anyMatch(locations -> !Collections.disjoint(locations, list));
    }

    Boolean hasShotAt(GridLocation location) {
        return shotsFired.get(location);
    }

    boolean wasShotAt(GridLocation location) {
        return shotsReceived.contains(location);
    }

    void addShip(Ship ship, Placement placement) {
        var locations = GridLocation.range(placement.start(), placement.end()).collect(Collectors.toCollection(HashSet::new));
        ships.put(ship, locations);
    }

    void addShot(GridLocation target, boolean result) {
        shotsFired.put(target, result);
    }

    ShotResult getShot(GridLocation target) {
        shotsReceived.add(target);
        var ship = shipAt(target);
        if (ship == null) return ShotResult.MISS;
        if(ships.values().stream().allMatch(shipLocations -> shotsReceived.containsAll(shipLocations))) {
            return ShotResult.WON;
        } else if (shotsReceived.containsAll(ships.get(ship))) {
            return ShotResult.SUNK;
        }
        return ShotResult.HIT;
    }
}
