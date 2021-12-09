package fr.iut2.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.iut2.tictactoe.domain.models.Game;
import fr.iut2.tictactoe.domain.models.players.IPlayer;
import fr.iut2.tictactoe.helpers.BitmapExtension;
import fr.iut2.tictactoe.helpers.layouts.GridFragment;

public class GameActivity extends AppCompatActivity {

    private final Game game;

    private TextView username;
    private ImageView photo;

    private ScrollView container;

    public GameActivity() {
        game = Game.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        username = findViewById(R.id.activity_game_player);
        photo = findViewById(R.id.activity_game_player_photo);

        container = findViewById(R.id.activity_game_grid);

        refreshPlayer(game.getCurrentPlayer());
        init();
    }

    private void init() {
        GridFragment fragment = new GridFragment(this, game.getGrid());

        fragment.beforeClick((coordinate) -> game.isWaiting());
        fragment.onClick(game::play);
        fragment.afterClick((coordinate) -> {

            runOnUiThread(() -> refreshPlayer(game.getCurrentPlayer()));

            if (game.isWaiting()) {
                if (game.getCurrentPlayer().isComputer())
                    fragment.simulate();
            } else {
                runOnUiThread(this::end);
            }
        });

        container.addView(fragment);
    }

    private void refreshPlayer(IPlayer player) {

        username.setText(getString(R.string.activity_game_waiting).replace("X", player.getUsername()));
        photo.setImageBitmap(BitmapExtension.fromBase64(player.getPhoto()));
    }

    private void end() {
        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        finish();
    }
}