package fr.iut2.tictactoe.domain.models.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.iut2.tictactoe.domain.models.players.Player;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM players")
    List<Player> get();

    @Query("SELECT * FROM players WHERE username LIKE :username LIMIT 1")
    Player get(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Player... players);
}
