package base;

import org.jetbrains.annotations.NotNull;


public interface GameMechanics {
    int getMyScore(String user);

    int getEnemyScore(String user);

    String getEnemyName(String user);

    void addUser(@NotNull String user);

    void removeGameSession(@NotNull String user);

    void removeUser(@NotNull String user);

    void incrementScore(String userName);

    void starGame(@NotNull String first, @NotNull String second);

    void gameOver(String user);
}
