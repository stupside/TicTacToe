package fr.iut2.tictactoe.domain.models;

import java.util.Arrays;
import java.util.Objects;

import fr.iut2.tictactoe.domain.enums.GameState;
import fr.iut2.tictactoe.domain.models.players.Computer;
import fr.iut2.tictactoe.domain.models.players.IPlayer;
import fr.iut2.tictactoe.domain.models.players.Player;

public class Game {

    public final static int[] PLAYER_IDS = new int[]{Pid.P1.ordinal(), Pid.P2.ordinal(), /*Pid.P3.ordinal()*/};
    private static Game Instance;
    private final Grid grid;
    private IPlayer[] players;
    private GameState state;
    private Integer round;
    private Game(Integer size) {

        grid = new Grid(size);

        reset();
    }

    // Singleton get
    public static Game get() {
        return Instance;
    }

    // Singleton create
    public static Game create(Integer size) {
        // Calling newGame will override the Game instance
        Instance = new Game(size);

        return get();
    }

    public void setPlayer(Integer id, IPlayer player) {

        if (isUsernameTaken(player.getUsername()))
            throw new IllegalArgumentException("Username already taken");

        this.players[id] = player;
    }

    public Integer getCurrentRound() {
        return round;
    }

    public IPlayer getNextPlayer() {
        // There is PLAYER_IDS.length players and one round per player, meaning round % PLAYER_IDS.length is the id of the current player
        return getPlayer(getCurrentRound() + 1);
    }

    public IPlayer getCurrentPlayer() {
        // There is PLAYER_IDS.length players and one round per player, meaning round % PLAYER_IDS.length is the id of the current player
        return getPlayer(getCurrentRound());
    }

    public IPlayer getPlayer(Integer round) {
        // There is PLAYER_IDS.length players and one round per player so we maximize id by PLAYER_IDS.length-1
        int mod = round % PLAYER_IDS.length;
        return players[mod];
    }

    public IPlayer[] getAllPlayers() {
        return players;
    }

    public Player[] getRealPlayers() {
        return Arrays.stream(players).filter((player) -> player instanceof Player)
                .map(Player.class::cast)
                .filter(Objects::nonNull)
                .filter((player) -> !player.isDefault()).toArray(Player[]::new);
    }

    public Grid getGrid() {
        return grid;
    }

    public Cell play(Coordinate coordinate) {

        if (!isWaiting())
            return null;

        IPlayer player = getCurrentPlayer();

        Cell cell = grid.selectCell(coordinate, player);

        if (isWinAtCoordinate(coordinate, player)) {
            state = GameState.WIN;
        } else if (getCurrentRound() >= grid.getSize() * grid.getSize() - 1) {
            state = GameState.EQUALITY;
        } else {
            nextRound();
        }

        return cell;
    }

    public GameState getGameState() {
        return state;
    }

    public boolean isWinAtCoordinate(Coordinate coordinate, IPlayer player) {
        Grid grid = getGrid();
        return grid.isHorizontalSelected(coordinate.getY(), player)
                || grid.isVerticalSelected(coordinate.getX(), player)
                || grid.anyDiagonalsSelected(player);
    }

    public boolean isEquality() {
        return getGameState().equals(GameState.EQUALITY);
    }

    public boolean isWin() {
        return getGameState().equals(GameState.WIN);
    }

    public boolean isWaiting() {
        return getGameState().equals(GameState.WAITING);
    }

    public boolean isUsernameTaken(String username) {

        if (username.equals(Player.DEFAULT_USERNAME))
            return false;

        return Arrays.stream(getAllPlayers()).anyMatch((player) ->
                player != null
                        && player.getUsername().equals(username)
                        && !player.getUsername().equals(Player.DEFAULT_USERNAME)
                        && !player.getUsername().equals(Computer.DEFAULT_COMPUTER_NAME));
    }

    public void nextRound() {
        round++;
    }

    public void reset() {

        round = 0;
        players = new IPlayer[PLAYER_IDS.length];
        state = GameState.WAITING;

        grid.reset();
    }

    public enum Pid {
        P1,
        P2,
        /*P3*/ // TODO: More than 2 players
    }
}
