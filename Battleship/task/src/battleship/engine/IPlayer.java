package battleship.engine;

public interface IPlayer {
    public void placeShip(Ship ship);
    public void shoot();
    public void startPlacement();
    public void startShooting();
    public void endMove();
}
