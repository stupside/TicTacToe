package fr.iut2.tictactoe.domain.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import fr.iut2.tictactoe.domain.DbContext;

public class DatabaseClient {

    private static final String DB_NAME = "TicTacToe";

    private static DatabaseClient instance;

    private final DbContext context;

    RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private DatabaseClient(final Context context) {
        this.context = Room.databaseBuilder(context, DbContext.class, DB_NAME).build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public DbContext getContext() {
        return context;
    }
}
