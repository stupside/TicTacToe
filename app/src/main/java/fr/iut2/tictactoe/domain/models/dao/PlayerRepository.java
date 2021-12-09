package fr.iut2.tictactoe.domain.models.dao;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import fr.iut2.tictactoe.domain.DbContext;
import fr.iut2.tictactoe.domain.models.players.Player;

public class PlayerRepository {

    private final DbContext _context;

    public PlayerRepository(DbContext context) {
        _context = context;
    }

    public CompletableFuture<List<Player>> get() {

        return CompletableFuture.supplyAsync(() -> _context.Users().get());
    }

    public CompletableFuture<Player> get(String username) {
        return CompletableFuture.supplyAsync(() -> _context.Users().get(username));
    }

    public CompletableFuture<Void> upsert(Player... players) {
        return CompletableFuture.runAsync(() -> {
            _context.Users().insert(players);
        });
    }
}
