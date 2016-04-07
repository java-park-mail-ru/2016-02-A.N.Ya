package main;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import rest.Sessions;
import rest.Users;
import services.AccountService;
import services.AccountServiceOnHibernate;
import services.SessionService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.HashSet;
import java.util.Set;


public class Main {
    private static Context context;

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port = -1;
        if (args.length == 1) {
            port = Integer.valueOf(args[0]);
        } else {
            System.err.println("Specify port");
            System.exit(1);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        final Server server = new Server(port);
        final ServletContextHandler contextHandler =
                new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);


        AccountServiceOnHibernate accountService = new AccountServiceOnHibernate();
        SessionService sessionService = new SessionService();

        if(!accountService.isConnected()) {
            System.err.println("Couldn't connect to database");
            System.exit(1);
        }

        context = new Context();
        context.add(AccountService.class, accountService);
        context.add(SessionService.class, sessionService);

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
        server.setHandler(contextHandler);

        server.start();
        System.out.println("Server started");
        server.join();
    }
}