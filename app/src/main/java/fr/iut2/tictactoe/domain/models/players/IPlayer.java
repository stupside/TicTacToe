package fr.iut2.tictactoe.domain.models.players;

import fr.iut2.tictactoe.domain.models.Game;

public interface IPlayer {
    boolean isComputer();

    String getUsername();

    String getPhoto();

    void setPhoto(String photo);

    void score(Game game);

    Integer getVictories();

    Integer getEqualities();

    Integer getDefeats();

    void reset();
}
