package fr.iut2.tictactoe.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fr.iut2.tictactoe.domain.models.players.IPlayer;

public class Grid {

    public final static Integer MIN_GRID_SIZE = 3;
    public final static Integer MAX_GRID_SIZE = 5;

    private final Integer size;
    private final Stack<Coordinate> history;

    private List<Cell> cells;

    public Grid(Integer size) {

        if (size > MAX_GRID_SIZE)
            size = MAX_GRID_SIZE;
        else if (size < MIN_GRID_SIZE)
            size = MIN_GRID_SIZE;

        this.size = size;
        history = new Stack<>();

        init();
    }

    public Integer getSize() {
        return size;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Cell getCell(Coordinate coordinate) throws IllegalArgumentException {

        Optional<Cell> cell = getCells().stream()
                .filter((_cell) -> _cell.getCoordinate().getX().equals(coordinate.getX()) && _cell.getCoordinate().getY().equals(coordinate.getY()))
                .findFirst();

        if (!cell.isPresent())
            throw new IllegalArgumentException("Invalid coordinates supplied");

        return cell.get();
    }

    public Coordinate getLastCellCoordinate() {
        return history.peek();
    }

    public boolean isHorizontalSelected(Integer y, IPlayer player) {
        List<Cell> cells = getCells().stream().filter((cell) -> cell.getCoordinate().getY().equals(y)).collect(Collectors.toList());
        return cells.stream().allMatch((cell) -> cell.isSelectedByPlayer(player));
    }

    public boolean isVerticalSelected(Integer x, IPlayer player) {
        // We get the vertical X, if one cell is not selected by the player, it's not a win
        for (int y = 0; y < getSize(); y++) {
            Cell cell = getCell(Coordinate.from(x, y));
            // If not current cell is not selected by the player, we short cut
            if (!cell.isSelectedByPlayer(player))
                return false;
        }

        // If we go that far, it means every cell are selected by the player
        return true;
    }

    public boolean anyDiagonalsSelected(IPlayer player) {

        int size = getSize() - 1;

        // Right to left
        for (int y = 0; y <= size; y++) {

            if (!getCell(Coordinate.from(y, y)).isSelectedByPlayer(player))
                break;

            if (y == size)
                return true;
        }

        // Left to right
        for (int y = size; y >= 0; y--) {

            if (!getCell(Coordinate.from(size - y, y)).isSelectedByPlayer(player))
                break;

            if (y == 0)
                return true;
        }

        return false;
    }

    public Cell selectCell(Coordinate coordinate, IPlayer player) {

        // Mark the cell as selected
        Cell cell = getCell(coordinate);
        cell.select(player);

        // Push the selected coordinate to the coordinate history
        history.push(coordinate);

        return cell;
    }

    public List<Coordinate> getFreeCoordinates() {
        Grid grid = Game.get().getGrid();

        List<Coordinate> free = new ArrayList<>();

        grid.forEachCells((cell) -> {
            if (!cell.isSelected())
                free.add(cell.getCoordinate());
        });

        return free;
    }

    public void forEachCells(Consumer<Cell> consumer) {
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                Cell cell = getCell(Coordinate.from(x, y));
                consumer.accept(cell);
            }
        }
    }

    private void init() {
        cells = new ArrayList<>();
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                cells.add(new Cell(Coordinate.from(x, y)));
            }
        }
    }

    public void reset() {
        history.empty();
        forEachCells(Cell::reset);
    }

}
