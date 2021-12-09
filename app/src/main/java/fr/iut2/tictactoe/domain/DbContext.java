package fr.iut2.tictactoe.domain;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.iut2.tictactoe.domain.models.dao.PlayerDao;
import fr.iut2.tictactoe.domain.models.players.Player;

@Database(entities = {Player.class}, version = 1, exportSchema = false)
public abstract class DbContext extends RoomDatabase {
    public abstract PlayerDao Users();
}
