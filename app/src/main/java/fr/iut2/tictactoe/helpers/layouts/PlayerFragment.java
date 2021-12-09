package fr.iut2.tictactoe.helpers.layouts;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.function.Consumer;

import fr.iut2.tictactoe.R;
import fr.iut2.tictactoe.domain.db.DatabaseClient;
import fr.iut2.tictactoe.domain.models.dao.PlayerRepository;
import fr.iut2.tictactoe.domain.models.players.Player;
import fr.iut2.tictactoe.helpers.BitmapExtension;

public class PlayerFragment extends LinearLayout {

    private final PlayerRepository repository;
    private Player player;
    private Consumer<Player> onRemove;
    private Consumer<PlayerFragment> onPhoto;

    public PlayerFragment(Context context, Player player) {
        super(context);

        repository = new PlayerRepository(DatabaseClient.getInstance(context).getContext());

        setPlayer(player);

        init();
        syncLayout(true);
    }

    public void onRemove(Consumer<Player> consumer) {
        this.onRemove = consumer;
    }

    public void onPhoto(Consumer<PlayerFragment> consumer) {
        this.onPhoto = consumer;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_main_player, this);

        ImageView image = findViewById(R.id.activity_main_player_photo);
        image.setOnClickListener((button) -> onPhoto.accept(this));

        EditText username = findViewById(R.id.activity_main_player_pseudo);
        if (!player.isDefault()) {
            username.setFocusable(false);
        } else username.addTextChangedListener(onUsernameChanged());

        Button remove = findViewById(R.id.activity_main_remove_player);
        remove.setOnClickListener((button) -> {
            onRemove.accept(player);
        });
    }

    public void syncLayout(Boolean username) {
        if (username) {
            EditText editText = findViewById(R.id.activity_main_player_pseudo);
            editText.setText(player.getUsername());
        }

        {
            ImageView imageView = findViewById(R.id.activity_main_player_photo);

            if (player.getPhoto() == null || player.getPhoto().isEmpty()) {
                imageView.setImageResource(R.drawable.photo);
                player.setPhoto(BitmapExtension.toBase64(BitmapExtension.fromImageView(imageView)));
            } else {
                imageView.setImageBitmap(BitmapExtension.fromBase64(player.getPhoto()));
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    private void setPlayer(Player player) {
        this.player = player == null ? Player.get() : player;
    }

    private TextWatcher onUsernameChanged() {
        return new TextWatcher() {

            String username = player.getUsername();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (username.equals(s.toString()))
                    return;

                repository.get(s.toString()).thenAccept((player) -> {

                    username = s.toString();

                    if (player == null)
                        setPlayer(new Player(username));
                    else
                        setPlayer(player);

                    syncLayout(false);
                });
            }
        };
    }
}
