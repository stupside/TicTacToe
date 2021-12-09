package fr.iut2.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import fr.iut2.tictactoe.domain.db.DatabaseClient;
import fr.iut2.tictactoe.domain.models.Game;
import fr.iut2.tictactoe.domain.models.Grid;
import fr.iut2.tictactoe.domain.models.dao.PlayerRepository;
import fr.iut2.tictactoe.domain.models.players.Computer;
import fr.iut2.tictactoe.domain.models.players.IPlayer;
import fr.iut2.tictactoe.domain.models.players.Player;
import fr.iut2.tictactoe.fragments.NumberPickerDialog;
import fr.iut2.tictactoe.helpers.BitmapExtension;
import fr.iut2.tictactoe.helpers.layouts.PlayerFragment;

public class MainActivity extends AppCompatActivity {

    private PlayerRepository repository;

    private PlayerFragment fragment;
    private final ActivityResultLauncher<Intent> camera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (result) -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data == null)
                        return;

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    if (fragment == null)
                        return;

                    fragment.getPlayer().setPhoto(BitmapExtension.toBase64(bitmap));
                    fragment.syncLayout(false);
                }
            });
    private Player spinner;
    private NumberPickerDialog dialog;
    private HashSet<PlayerFragment> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new PlayerRepository(DatabaseClient.getInstance(getApplicationContext()).getContext());

        players = new HashSet<>();

        dialog = new NumberPickerDialog(Grid.MIN_GRID_SIZE, Grid.MAX_GRID_SIZE, Grid.MIN_GRID_SIZE);
        dialog.setOnChange((size) -> {
            Button button = findViewById(R.id.activity_main_dialog_grid_size);
            button.setText(getString(R.string.activity_main_dialog_grid_size).replace("Y", String.valueOf(size)));
        });
        dialog.callOnChange();

        // ADD PLAYER

        findViewById(R.id.activity_main_add_player).setOnClickListener((button) -> addPlayer(spinner));

        // ADD PLAYER

        // SPINNER
        Spinner spinner = findViewById(R.id.activity_main_player_spinner);
        repository.get().thenAccept((players) -> {

            List<String> usernames = players.stream().map(Player::getUsername).collect(Collectors.toList());
            usernames.add(Player.DEFAULT_USERNAME);

            runOnUiThread(() -> {
                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, usernames));
                spinner.setOnItemSelectedListener(onPlayerUsernameSelected());
            });
        });
        // SPINNER
    }

    public void onDialogSize(View view) {
        dialog.show(getSupportFragmentManager(), null);
    }

    public void onPlay(View view) {

        Game game = Game.create(dialog.getValue());

        Player[] array = players.stream().map(PlayerFragment::getPlayer).toArray(Player[]::new);

        if (array.length == 0)
            return;

        for (int index = 0; index < Game.PLAYER_IDS.length; index++) {

            IPlayer player;
            if (index > array.length - 1) {
                player = Computer.get();

                BitmapDrawable drawable = (BitmapDrawable) AppCompatResources.getDrawable(this, R.drawable.robot);

                if (drawable != null)
                    player.setPhoto(BitmapExtension.toBase64(drawable.getBitmap()));

            } else {
                player = array[index];
            }

            try {
                game.setPlayer(index, player);
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();

                game.reset();
                return;
            }
        }

        repository.upsert(game.getRealPlayers());

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void addPlayer(Player player) {

        if (player == null)
            player = Player.get();

        String username = player.getUsername();

        if (players.stream()
                .filter((fragment) -> !fragment.getPlayer().getUsername().equals(Player.DEFAULT_USERNAME) || !fragment.getPlayer().getUsername().equals(Computer.DEFAULT_COMPUTER_NAME))
                .anyMatch((fragment) -> fragment.getPlayer().getUsername().equals(username)))
            return;

        if (players.size() == Game.PLAYER_IDS.length)
            return;

        LinearLayout layout = findViewById(R.id.activity_main_players);

        PlayerFragment fragment = new PlayerFragment(this, player);

        fragment.onRemove((_player) -> removePlayer(fragment));
        fragment.onPhoto((_fragment) -> {
            camera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            this.fragment = _fragment;
        });

        players.add(fragment);

        layout.addView(fragment);

        if (players.size() == Game.PLAYER_IDS.length)
            findViewById(R.id.activity_main_add_player).setVisibility(View.INVISIBLE);
    }

    public void removePlayer(PlayerFragment fragment) {

        LinearLayout layout = findViewById(R.id.activity_main_players);

        layout.removeView(fragment);
        players.remove(fragment);


        if (players.size() < Game.PLAYER_IDS.length)
            findViewById(R.id.activity_main_add_player).setVisibility(View.VISIBLE);
    }

    private AdapterView.OnItemSelectedListener onPlayerUsernameSelected() {

        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String username = parent.getItemAtPosition(position).toString();

                repository.get(username).thenAccept((player) -> spinner = player);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner = null;
            }
        };
    }
}