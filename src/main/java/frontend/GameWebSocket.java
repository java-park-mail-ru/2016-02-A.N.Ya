package frontend;

import base.AccountService;
import com.google.gson.*;
import game.GameUser;
import game.GameMechanicsImpl;
import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;


@WebSocket
public class GameWebSocket {
    private static final Logger LOGGER = LogManager.getLogger(GameWebSocket.class);

    @Nullable
    private Session session;
    @NotNull
    private final GameMechanicsImpl gameMechanics;
    private final WebSocketServiceImpl webSocketService;
    private final AccountService accountService;

    @NotNull
    private final String myName;
    public GameWebSocket(@NotNull String myName, Context context) {
        this.myName = myName;
        this.gameMechanics = context.get(GameMechanicsImpl.class);
        this.webSocketService = context.get(WebSocketServiceImpl.class);
        this.accountService = context.get(AccountService.class);
        LOGGER.info("Socket created for " + myName);


    }
    @NotNull
    public String getMyName() {
        return myName;
    }

    public void startGame(@NotNull GameUser user) {

        final JsonObject json = new JsonObject();
        json.addProperty("action", "startGame");
        json.addProperty("user", user.getMyName());
        json.addProperty("userRecord", accountService.getUser(user.getMyName()).getScore());
        json.addProperty("enemy", user.getEnemyName());
        json.addProperty("enemyRecord", accountService.getUser(user.getEnemyName()).getScore());

        sendJson(json);
    }

    public void gameOver(boolean equality, boolean win) {

        final JsonObject jsonEndGame = new JsonObject();
        jsonEndGame.addProperty("action", "finishGame");
        if (equality){
            jsonEndGame.addProperty("equality", true);
        } else {
            jsonEndGame.addProperty("win", win);
        }
        int currentScore = gameMechanics.getMyScore(myName);
        jsonEndGame.addProperty("myName", myName);
        jsonEndGame.addProperty("myScore", currentScore);
        jsonEndGame.addProperty("enemyName", gameMechanics.getEnemyName(myName));
        jsonEndGame.addProperty("enemyScore", gameMechanics.getEnemyScore(myName));

        int prevScore = accountService.getUser(myName).getScore();
        boolean isBestScore;

        if (currentScore > prevScore){
            accountService.editScore(myName,currentScore);

            isBestScore = true;
        } else {
            isBestScore = false;
        }
        jsonEndGame.addProperty("best", isBestScore);

        sendJson(jsonEndGame);

        //webSocketService.removeUser(this);
        gameMechanics.removeGameSession(myName);

    }

    public void finishGameEnemyleft() {
        final JsonObject json = new JsonObject();
        json.add("action", new JsonPrimitive("enemyLeft"));
        sendJson(json);

        webSocketService.removeUser(this);
        gameMechanics.removeGameSession(myName);
    }

    @SuppressWarnings("unused")
    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            final JsonElement jsonElement = new JsonParser().parse(data);
            final String action = jsonElement.getAsJsonObject().getAsJsonPrimitive("action").getAsString();
            if (action == null){
                throw new JsonSyntaxException("Can't find out \"action\" in JSON");
            }
            switch (action){
                case "incrementScore":
                    gameMechanics.incrementScore(myName);
                    final JsonObject json = new JsonObject();
                    json.addProperty("action", "incrementScore");
                    json.addProperty("myScore", gameMechanics.getMyScore(myName));
                    json.addProperty("enemyScore", gameMechanics.getEnemyScore(myName));
                    sendJson(json);
                    break;

                case "finishGame":

                    gameMechanics.gameOver(myName);
                    break;

                default:
                    throw new JsonSyntaxException("Unknown \"action\"");
            }
        } catch(JsonSyntaxException e){

            LOGGER.error("Can't find out \"action\" in JSON");
            final JsonObject jsonError = new JsonObject();
            jsonError.addProperty("error", "action is null");
            sendJson(jsonError);
        }
    }

    @SuppressWarnings({"ParameterHidesMemberVariable", "unused"})
    @OnWebSocketConnect
    public void onOpen(@NotNull Session session) {
        this.session = session;
        webSocketService.addUser(this);
        gameMechanics.addUser(myName);

    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        webSocketService.removeUser(this);
        gameMechanics.removeUser(myName);
        gameMechanics.removeGameSession(myName);
        LOGGER.info("Closing socket for: {}  status: {} reason: {}", myName, statusCode, reason);
    }

    public void sendJson(JsonObject json){
        try {
            if (session != null && session.isOpen())
                session.getRemote().sendString(json.toString());
        } catch (IOException | WebSocketException e){
            LOGGER.error("Can't send web socket", e);
        }
    }
}
