package fr.iut2.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.iut2.tictactoe.domain.db.DatabaseClient;
import fr.iut2.tictactoe.domain.models.Game;
import fr.iut2.tictactoe.domain.models.dao.PlayerRepository;
import fr.iut2.tictactoe.domain.models.players.Player;

public class ScoreActivity extends AppCompatActivity {

    private final Game game;

    public ScoreActivity() {
        game = Game.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        PlayerRepository repository = new PlayerRepository(DatabaseClient.getInstance(getApplicationContext()).getContext());

        init();

        LinearLayout layout = findViewById(R.id.activity_score_list);

        for (Player player : game.getRealPlayers())
            player.score(game);

        repository.upsert(game.getRealPlayers()).thenRunAsync(() ->
                repository.get().thenAccept((players) ->
                        players.stream().sorted().forEach((player) ->
                                runOnUiThread(() -> {
                                    TextView score = new TextView(this);

                                    // TODO: Really need to make something better. That's hella ugly but had to print the score ASAP
                                    score.setText(String.format("\n%s V: %d E: %d D: %d", player.getUsername(), player.getVictories(), player.getEqualities(), player.getDefeats()));
                                    layout.addView(score);
                                })
                        )
                )
        );
    }

    private void init() {

        String message = game.isWin() ? getString(R.string.activity_score_kind_victory) : getString(R.string.activity_score_kind_equality);

        TextView score_kind = findViewById(R.id.activity_score_kind);
        score_kind.setText(message
                .replace("X", game.getCurrentPlayer().getUsername())
                .replace("Y", game.getNextPlayer().getUsername())
        );
    }

    public void onPlay(View view) {

        game.reset();

        Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }
}