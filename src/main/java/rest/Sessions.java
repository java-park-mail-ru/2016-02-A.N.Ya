package rest;


import account.SessionServiceImpl;
import account.UserProfile;
import base.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by morev on 02.03.16.
 */

@Singleton
@Path("/session")
public class Sessions {
    private static final Logger logger = LogManager.getLogger(Sessions.class);
    @Inject private main.Context context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response isLogged(@Context HttpServletRequest request) {
        System.out.println("Sessions - get - isLogged \"" + request.getSession().getId() + '"');
        final SessionServiceImpl sessionService = (SessionServiceImpl) context.get(SessionServiceImpl.class);
        final String id = request.getSession().getId();
        final UserProfile user = sessionService.getUserById(id);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            final String responseString = Json.createObjectBuilder()
                                            .add("id", user.getId())
                                            .build()
                                            .toString();
            return Response.status(Response.Status.OK).entity(responseString).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserProfile session, @Context HttpServletRequest request){
        System.out.println("Sessions - put - login \"" + session.getLogin() + '"');
        final SessionServiceImpl sessionService = (SessionServiceImpl) context.get(SessionServiceImpl.class);
        final AccountService accountService = (AccountService) context.get(AccountService.class);


        final UserProfile user = accountService.getUser(session.getLogin());


        if (user == null) {
            System.out.println("No such user");
            return Response.status(Response.Status.NO_CONTENT).build();
        }


        if ((user != null) && (user.getPassword().equals(session.getPassword()))){
            sessionService.newSession(request.getSession().getId(), user);
            final String json = Json.createObjectBuilder()
                    .add("id", user.getId())
                    .build()
                    .toString();
            return Response.status(Response.Status.OK).entity(json).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    public Response logout(@Context HttpServletRequest request){
        System.out.println("Sessions - delete - logout");
        final SessionServiceImpl sessionService = (SessionServiceImpl) context.get(SessionServiceImpl.class);
        final String id = request.getSession().getId();
        sessionService.deleteSession(id);
        return Response.status(Response.Status.OK).build();
    }
}
