package battleship.engine;

public class GameLoop {

    public GameLoop(GameState game, IPlayer ...players) {
        for (var player :
                players) {
            game.addPlayer(player);
        }
        for (var player :
                players) {
            game.nextPlayer();
            player.startPlacement();
            for (var ship: game.getShips()) {
                player.placeShip(ship);
            }
            player.endMove();
//            player.startShooting();
        }
        while(!game.isFinished()) {
            game.nextPlayer();
            game.player.shoot();
            game.player.endMove();
        }
    }
}
