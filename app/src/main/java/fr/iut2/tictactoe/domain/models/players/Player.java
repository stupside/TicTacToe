package fr.iut2.tictactoe.domain.models.players;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.security.InvalidParameterException;

import fr.iut2.tictactoe.domain.models.Game;

@Entity(tableName = "players")
public class Player implements IPlayer, Comparable<Player> {

    public static String DEFAULT_USERNAME = "default";

    public static Integer USERNAME_MIN_SIZE = 4;
    public static Integer USERNAME_MAX_SIZE = 10;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    protected String username;

    @ColumnInfo(name = "photo")
    protected String photo;

    @ColumnInfo(name = "victories")
    protected Integer victories;

    @ColumnInfo(name = "equalities")
    protected Integer equalities;

    @ColumnInfo(name = "defeats")
    protected Integer defeats;

    public Player(String username) {

        if (username.isEmpty())
            username = DEFAULT_USERNAME;
        else if (username.length() > USERNAME_MAX_SIZE)
            username = username.substring(0, USERNAME_MAX_SIZE - 1);
        else if (username.length() < USERNAME_MIN_SIZE)
            throw new InvalidParameterException("Username is too short");

        this.username = username;
    }

    public static Player get() {
        return new Player(Player.DEFAULT_USERNAME);
    }

    public boolean isComputer() {
        return false;
    }

    public boolean isDefault() {
        return getUsername().equals(DEFAULT_USERNAME);
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void score(Game game) {
        if (game.isWin()) {
            if (game.getCurrentPlayer().getUsername().equals(getUsername())) {
                setVictories(getVictories() + 1);
            } else {
                setDefeats(getDefeats() + 1);
            }
        } else if (game.isEquality()) {
            setEqualities(getEqualities() + 1);
        }
    }

    public Integer getVictories() {
        return victories == null ? 0 : victories;
    }

    public void setVictories(Integer victories) {
        this.victories = victories;
    }

    public Integer getEqualities() {
        return equalities == null ? 0 : equalities;
    }

    public void setEqualities(Integer equalities) {
        this.equalities = equalities;
    }

    public Integer getDefeats() {
        return defeats == null ? 0 : defeats;
    }

    public void setDefeats(Integer defeats) {
        this.defeats = defeats;
    }

    public void reset() {
        setVictories(0);
        setEqualities(0);
        setDefeats(0);
    }

    @Override
    public int compareTo(Player other) {

        if (getVictories() > other.getVictories())
            return 1;

        if (getDefeats() > other.getDefeats())
            return -1;

        return 0;
    }
}
