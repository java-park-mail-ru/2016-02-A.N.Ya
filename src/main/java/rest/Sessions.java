package rest;

import main.AccountService;
import main.SessionService;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by morev on 02.03.16.
 */

@Singleton
@Path("/session")
public class Sessions {
    private AccountService accountService;
    private SessionService sessionService;


    public Sessions(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response isLogged(@Context HttpServletRequest request) {
        String id = request.getSession().getId();
        UserProfile user = sessionService.getUserById(id);
        if ( user == null )
            return Response.status(Response.Status.UNAUTHORIZED).build();
        else
            return Response.status(Response.Status.OK).entity("{ \"id\": " + id + "}").build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("login") String login,
                          @FormParam("password") String password,
                          @Context HttpServletRequest request){
        UserProfile user = accountService.getUser(login);
        if (user.getPassword() == password){
            sessionService.newSession(request.getSession().getId(), user);
            return Response.status(Response.Status.OK).entity("{ \"id\": " + user.getId() + "}").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    public Response logout(@Context HttpServletRequest request){
        String id = request.getSession().getId();
        sessionService.deleteSession(id);
        return Response.status(Response.Status.OK).build();
    }
}
