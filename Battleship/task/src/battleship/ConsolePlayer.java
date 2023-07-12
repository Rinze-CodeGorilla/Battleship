package battleship;

import battleship.engine.*;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The Player knows about his own ships and shots
 */
public class ConsolePlayer implements battleship.engine.IPlayer {

    private final GameAPI game;
    private final String name;
    Scanner scanner = new Scanner(System.in);

    public ConsolePlayer(GameAPI game, String name) {
        this.game = game;
        this.name = name;
    }

    @Override
    public void placeShip(Ship ship) {
        System.out.format("Enter the coordinates of the %s (%d cells):\n",ship.name(), ship.size());
        var correct = false;
        while(!correct) {
            try {
                game.placeShip(ship, new Placement(new GridLocation(scanner.next()), new GridLocation(scanner.next())));
                correct = true;
            } catch (PlacementError error) {
                System.out.println("Error: " + error.getMessage() + ". Try again...");
            }
        }
        printBoard();
        // parse input beyond new line
        scanner.nextLine();
    }

    @Override
    public void shoot() {
        printOpponentBoard();
        System.out.println("----------------------");
        printBoard();
        System.out.println(name + ", it's your turn:");
        var correct = false;
        while (!correct) {
            try {
                var result = switch (game.shoot(getTarget())) {
                    case MISS -> "You missed!";
                    case HIT -> "You hit a ship!";
                    case SUNK -> "You sank a ship!";
                    case WON -> "You sank the last ship. You won. Congratulations!";
                };
//                printOpponentBoard();
                System.out.println(result);
                correct = true;
            }
            catch (ShootingError error) {
                System.out.println("Error! " + error.getMessage() + "! Try again:");
            }
        }
        // parse input beyond new line
        scanner.nextLine();
    }

    @Override
    public void startPlacement() {
        System.out.println(name + ", place your ships on the game field");
        printBoard();
    }

    @Override
    public void endMove() {
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
    }

    @Override
    public void startShooting() {
        System.out.println("The game starts!");
        printOpponentBoard();

        System.out.println("Take a shot!");
    }

    private GridLocation getTarget() {
        return new GridLocation(scanner.next());
    }

    private void printBoard() {
        System.out.print("  ");
        System.out.println(String.join(" ", IntStream.rangeClosed(1, game.getWidth()).mapToObj(Integer::toString).toList()));
        System.out.println(IntStream.range(0, game.getHeight()).mapToObj(y -> (char) ('A' + y) + " " + IntStream.range(0, game.getWidth()).mapToObj(x -> {
            var xy = new GridLocation(y, x);
            return switch(game.playerStateAt(xy)) {
                case HIT -> "X";
                case MISS -> "M";
                case SHIP -> "O";
                case WATER -> "~";
            };
        }).collect(Collectors.joining(" "))).collect(Collectors.joining("\n")));
    }

    private void printOpponentBoard() {
        System.out.print("  ");
        System.out.println(String.join(" ", IntStream.rangeClosed(1, game.getWidth()).mapToObj(Integer::toString).toList()));
        System.out.println(IntStream.range(0, game.getHeight()).mapToObj(y -> (char) ('A' + y) + " " + IntStream.range(0, game.getWidth()).mapToObj(x -> {
            var xy = new GridLocation(y, x);
            return switch (game.opponentStateAt(xy)) {
                case HIT -> "X";
                case MISS -> "M";
                case SHIP -> "O";
                case WATER -> "~";
            };
        }).collect(Collectors.joining(" "))).collect(Collectors.joining("\n")));
    }
}
