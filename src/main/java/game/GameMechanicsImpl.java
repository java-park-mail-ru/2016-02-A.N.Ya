package game;

import base.GameMechanics;
import frontend.WebSocketServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.*;

public class GameMechanicsImpl implements GameMechanics {

    private static final Logger LOGGER = LogManager.getLogger(GameMechanicsImpl.class);

    @NotNull
    private final WebSocketServiceImpl webSocketService;

    @NotNull
    private final Map<String, GameSession> nameToGame = new HashMap<>();


    @Nullable
    private volatile String waiter;

    public GameMechanicsImpl(@NotNull WebSocketServiceImpl webSocketService) {
        this.webSocketService = webSocketService;

    }

    @Override
    public int getMyScore(String user){
        return nameToGame.get(user).getMyScore(user);
    }

    @Override
    public int getEnemyScore(String user){
        return nameToGame.get(user).getEnemyScore(user);
    }

    @Override
    public String getEnemyName(String user){
        return nameToGame.get(user).getEnemy(user).getMyName();
    }

    @Override
    public void addUser(@NotNull String user) {
        if (waiter != null) {
            //noinspection ConstantConditions
            starGame(user, waiter);
            waiter = null;
        } else {
            waiter = user;
        }
    }

    @Override
    public void removeGameSession(@NotNull String user){

        nameToGame.remove(user);
    }

    @Override
    public void removeUser(@NotNull String user) {
        if (waiter != null && user.equals(waiter)){
            waiter = null;
        }
        final GameSession myGameSession = nameToGame.get(user);
        if (myGameSession != null) {
            final GameUser enemyUser = myGameSession.getEnemy(user);
            webSocketService.notifyEnemyLeft(enemyUser);
        }
    }

    @Override
    public void incrementScore(String userName) {
        GameSession myGameSession = nameToGame.get(userName);
        GameUser myUser = myGameSession.getSelf(userName);
        myUser.incrementMyScore();
        GameUser enemyUser = myGameSession.getEnemy(userName);
        enemyUser.incrementEnemyScore();
    }

    @Override
    public void gameOver(String user){
        GameSession session = nameToGame.get(user);
        LOGGER.info("игра завершена для : " + session.getFirst().getMyName() + " и " + session.getSecond().getMyName());
        if (session.isEquality()) {
            webSocketService.notifyGameOver(session.getFirst(), true, false);
            webSocketService.notifyGameOver(session.getSecond(), true, false);
        } else {
            boolean firstWin = session.isFirstWin();
            webSocketService.notifyGameOver(session.getFirst(), false, firstWin);
            webSocketService.notifyGameOver(session.getSecond(), false, !firstWin);
        }
        nameToGame.values().removeAll(Collections.singleton(session));

    }

    @Override
    public void starGame(@NotNull String first, @NotNull String second) {
        GameSession gameSession = new GameSession(first, second);

        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getSelf(first));
        webSocketService.notifyStartGame(gameSession.getSelf(second));
    }

}
