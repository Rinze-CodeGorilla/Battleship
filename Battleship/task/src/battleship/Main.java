package battleship;

import battleship.engine.GameAPI;
import battleship.engine.GameLoop;
import battleship.engine.GameState;

public class Main {

    public static void main(String[] args) {
        var game = new GameState();
        var api = new GameAPI(game);
        var p1 = new ConsolePlayer(api, "Player 1");
        var p2 = new ConsolePlayer(api, "Player 2");
        new GameLoop(game, p1, p2);
    }

}
