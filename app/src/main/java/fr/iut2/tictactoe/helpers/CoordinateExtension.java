package fr.iut2.tictactoe.helpers;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import fr.iut2.tictactoe.domain.models.Coordinate;
import fr.iut2.tictactoe.domain.models.Grid;
import fr.iut2.tictactoe.domain.models.players.Computer;

public class CoordinateExtension {
    public static CompletableFuture<Coordinate> predictCoordinate(Grid grid) {

        // We create a thread for the computer, so we don't block the main thread
        return CompletableFuture.supplyAsync(() -> {
            List<Coordinate> coordinates = grid.getFreeCoordinates();

            // We sleep the thread... The bot is thinking
            try {
                Thread.sleep(Computer.DEFAULT_COMPUTER_THINKING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return coordinates.get(random(coordinates.size()));
        });
    }

    private static Integer random(Integer max) {
        Random random = new Random();

        int low = 0;
        int high = max;

        return random.nextInt(high - low) + low;
    }
}
