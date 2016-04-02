package rest;

import services.AccountService;
import services.SessionService;
import main.Main;

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
        System.out.println("RestApplication.getSingletones()");
        final HashSet<Object> objects = new HashSet<>();

        final AccountService accountService
                = (AccountService) Main.context.get(AccountService.class);
        final SessionService sessionService
                = (SessionService) Main.context.get(SessionService.class);

        objects.add(new Users(accountService, sessionService));
        objects.add(new Sessions(accountService, sessionService));

        return objects;
    }


}
