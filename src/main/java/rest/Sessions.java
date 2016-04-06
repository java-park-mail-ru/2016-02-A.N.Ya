package rest;

import services.AccountService;
import services.SessionService;

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
    private final AccountService accountService;
    private final SessionService sessionService;


    public Sessions(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response isLogged(@Context HttpServletRequest request) {
        System.out.println("Sessions - get - isLogged \"" + request.getSession().getId() + "\"");
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
        System.out.println("Sessions - put - login \"" + session.getLogin() + "\"");
        final UserProfile user = accountService.getUser(session.getLogin());
        if (user == null)
            System.out.println("No such user");
        if ((user != null) && (user.getPassword().equals(session.getPassword()))){
            sessionService.newSession(request.getSession().getId(), user);
            return Response.status(Response.Status.OK).entity("{ \"id\": " + user.getId() + '}').build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    public Response logout(@Context HttpServletRequest request){
        System.out.println("Sessions - delete - logout");
        final String id = request.getSession().getId();
        sessionService.deleteSession(id);
        return Response.status(Response.Status.OK).build();
    }
}
