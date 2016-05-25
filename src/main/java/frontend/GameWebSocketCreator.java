package frontend;

import account.SessionServiceImpl;
import main.Context;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class GameWebSocketCreator implements WebSocketCreator {
    private static final Logger LOGGER = LogManager.getLogger(GameWebSocketCreator.class);

    private final Context context;

    public GameWebSocketCreator(Context context) {
        this.context = context;
    }

    @Override
    @Nullable
    public GameWebSocket createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        try {
            String name = context.get(SessionServiceImpl.class).getUserById(sessionId).getLogin();
            LOGGER.info("name = " + name);
            return new GameWebSocket(name, context);

        } catch (NullPointerException e) {
            LOGGER.error("Can't create websocket, user don't authenticate");
            return null;
        }

    }
}
