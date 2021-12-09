package fr.iut2.tictactoe.domain.models;

import fr.iut2.tictactoe.domain.models.players.IPlayer;

public class Cell {

    private final Coordinate coordinate;
    private IPlayer player;

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public boolean isSelected() {
        return player != null;
    }

    public boolean isSelectedByPlayer(IPlayer user) {
        return getPlayer() == user;
    }

    public void select(IPlayer player) {

        if (isSelected())
            return;

        this.player = player;
    }

    public void reset() {
        player = null;
    }
}
