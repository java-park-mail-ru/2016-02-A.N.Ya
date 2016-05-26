package main;

import account.SessionServiceImpl;
import frontend.WebSocketGameServlet;
import frontend.WebSocketServiceImpl;
import game.GameMechanicsImpl;
import main.cfg.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.HibernateException;
import rest.Sessions;
import rest.Users;
import base.AccountService;
import account.AccountServiceOnHibernate;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.HashSet;
import java.util.Set;


public class Main {
    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static Context context;

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {

        Config.loadConfig();

        logger.info("Starting at port: " + String.valueOf(Config.getPort()));

        final Server server = new Server(Config.getPort());
        final ServletContextHandler contextHandler =
                new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);
        context = new Context();

        try {
            AccountServiceOnHibernate accountService = new AccountServiceOnHibernate();
            context.add(AccountService.class, accountService);
        } catch (HibernateException e){
            logger.error("Couldn't connect to database");
            System.exit(1);
        }

        SessionServiceImpl sessionService = new SessionServiceImpl();
        context.add(SessionServiceImpl.class, sessionService);
        context.add(WebSocketServiceImpl.class, new WebSocketServiceImpl());
        context.add(GameMechanicsImpl.class,new GameMechanicsImpl(context.get(WebSocketServiceImpl.class)));

        Set<Class<?>> classes = new HashSet<>();
        classes.add(Users.class);
        classes.add(Sessions.class);
        final ResourceConfig config = new ResourceConfig(classes);
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(context).to(Context.class);
            }
        });

        final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));

        contextHandler.addServlet(servletHolder, "/*");
        contextHandler.addServlet(new ServletHolder(new WebSocketGameServlet(context)), "/gameplay");

        server.setHandler(contextHandler);

        server.start();
        logger.info("Server started at port: " + String.valueOf(Config.getPort()) );
        server.join();
    }
}