package fr.iut2.tictactoe.domain.models;

public class Coordinate {

    private final Integer x;
    private final Integer y;

    public Coordinate(Integer x, Integer y) {

        if (x < 0) x = 0;
        if (y < 0) y = 0;

        this.x = x;
        this.y = y;
    }

    public static Coordinate from(Integer x, Integer y) {
        return new Coordinate(x, y);
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
