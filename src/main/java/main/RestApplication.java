package main;

import rest.Sessions;
import rest.Users;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by e.shubin on 25.02.2016.
 */
@ApplicationPath("api")
public class RestApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        final HashSet<Object> objects = new HashSet<>();
        AccountService accountService = new AccountService();
        SessionService sessionService = new SessionService();

        objects.add(new Users(accountService, sessionService));
        objects.add(new Sessions(accountService, sessionService));
        return objects;
    }
}
