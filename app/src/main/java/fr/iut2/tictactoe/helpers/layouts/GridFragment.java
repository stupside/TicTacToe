package fr.iut2.tictactoe.helpers.layouts;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import fr.iut2.tictactoe.domain.models.Cell;
import fr.iut2.tictactoe.domain.models.Coordinate;
import fr.iut2.tictactoe.domain.models.Grid;
import fr.iut2.tictactoe.helpers.BitmapExtension;
import fr.iut2.tictactoe.helpers.CoordinateExtension;

public class GridFragment extends GridLayout {

    private static final Integer DEFAULT_GRID_CELL_SIZE = 175;
    private static final Integer DEFAULT_GRID_CELL_COLOR = Color.LTGRAY;

    private final AtomicBoolean lock;

    private final Handler looper;

    private final HashMap<Cell, ImageButton> cells;
    private final Grid grid;
    private Function<Coordinate, Boolean> beforeClick;
    private Function<Coordinate, Cell> onClick;
    private Consumer<Coordinate> afterClick;

    public GridFragment(Context context, Grid grid) {
        super(context);

        setOrientation(android.widget.GridLayout.VERTICAL);
        setUseDefaultMargins(true);

        setColumnCount(grid.getSize());
        setRowCount(grid.getSize());

        setLayoutParams(new FrameLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.MATCH_PARENT));

        cells = new HashMap<>();
        looper = new Handler(Looper.getMainLooper());

        this.grid = grid;
        lock = new AtomicBoolean();

        init();
    }

    public void beforeClick(Function<Coordinate, Boolean> beforePlay) {
        this.beforeClick = beforePlay;
    }

    public void onClick(Function<Coordinate, Cell> onClick) {
        this.onClick = onClick;
    }

    public void afterClick(Consumer<Coordinate> afterPlay) {
        this.afterClick = afterPlay;
    }

    private void init() {

        grid.forEachCells((cell) -> {

            ImageButton button = new ImageButton(getContext());

            Coordinate coordinate = cell.getCoordinate();

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(coordinate.getY()), GridLayout.spec(coordinate.getX()));

            params.width = DEFAULT_GRID_CELL_SIZE;
            params.height = DEFAULT_GRID_CELL_SIZE;

            button.setLayoutParams(params);
            button.setBackgroundColor(DEFAULT_GRID_CELL_COLOR);

            cells.put(cell, button);

            refreshCell(cell);
            addView(button);
        });
    }

    public void onClick(Coordinate coordinate) {

        if (isLocked())
            return;

        if (!beforeClick.apply(coordinate))
            return;

        click(coordinate);
    }

    public void click(Coordinate coordinate) {

        Cell cell = onClick.apply(coordinate);

        looper.post(() -> refreshCell(cell));

        afterClick.accept(coordinate);
    }

    public void simulate() {
        lock.set(true);
        CoordinateExtension.predictCoordinate(grid)
                .thenAccept(this::click)
                .thenRun(() -> lock.set(false));
    }

    public void refreshCell(Cell cell) {

        ImageButton button = cells.get(cell);
        if (button == null)
            throw new NullPointerException();

        if (cell.isSelected()) {
            button.setOnClickListener(null);
            button.setImageBitmap(BitmapExtension.fromBase64(cell.getPlayer().getPhoto()));
        } else {
            button.setOnClickListener((e) -> onClick(cell.getCoordinate()));
            button.setImageDrawable(null);
        }
    }

    public void refreshCells() {
        grid.forEachCells(this::refreshCell);
    }

    public Boolean isLocked() {
        return lock.get();
    }

}
