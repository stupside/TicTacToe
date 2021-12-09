package fr.iut2.tictactoe.domain.models.players;

import fr.iut2.tictactoe.domain.models.Game;

public class Computer implements IPlayer {

    public static Integer DEFAULT_COMPUTER_THINKING_TIME = 1000;
    public static String DEFAULT_COMPUTER_NAME = "TicTacToe";

    public String photo;

    public static Computer get() {
        return new Computer();
    }

    public String getUsername() {
        return DEFAULT_COMPUTER_NAME;
    }

    public String getPhoto() {

        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void score(Game game) {
    }

    public Integer getVictories() {
        return 0;
    }

    public Integer getEqualities() {
        return 0;
    }

    public Integer getDefeats() {
        return 0;
    }

    public void reset() {

    }

    public boolean isComputer() {
        return true;
    }
}
