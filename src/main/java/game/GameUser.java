package game;

import org.jetbrains.annotations.NotNull;



public class GameUser {

    private static final int POINT = 10;
    @NotNull
    private final String myName;
    @NotNull
    private String enemyName;
    private int myScore = 0;
    private int enemyScore = 0;


    public GameUser(@NotNull String myName, @NotNull String enemyName) {

        this.myName = myName;
        this.enemyName = enemyName;
    }

    @NotNull
    public String getMyName() {
        return myName;
    }
    @NotNull
    public String getEnemyName() {
        return enemyName;
    }

    public int getMyScore() {
        return myScore;
    }

    @SuppressWarnings("unused")
    public int getEnemyScore() {
        return enemyScore;
    }

    public void incrementMyScore() {
        myScore += POINT;
    }

    public void incrementEnemyScore() {
        enemyScore += POINT;
    }

    @SuppressWarnings("unused")
    public void setEnemyName(@NotNull String enemyName) {
        this.enemyName = enemyName;
    }


}
