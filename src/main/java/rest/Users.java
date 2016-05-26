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
import java.util.Collection;


@Singleton
@Path("/user")
public class Users {
    private static final Logger logger = LogManager.getLogger(Users.class);
    @Inject private main.Context context;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile user, @Context HttpServletRequest request)  throws Exception {
        logger.info("Users - put - createUser \"" + user.getLogin() + '"');
        final AccountService accountService = (AccountService) context.get(AccountService.class);
        final SessionServiceImpl sessionService = (SessionServiceImpl) context.get(SessionServiceImpl.class);
        final long id = accountService.addUser(user);
        if(id != -1){
            String json = Json.createObjectBuilder()
                    .add("id", id)
                    .build()
                    .toString();
            sessionService.newSession(request.getSession().getId(), user);
            return Response.status(Response.Status.OK).entity(json).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") long id, @Context HttpServletRequest request) {
        logger.info("Users - get - getUserById " + id);
        final AccountService accountService = (AccountService) context.get(AccountService.class);
        final SessionServiceImpl sessionService = (SessionServiceImpl) context.get(SessionServiceImpl.class);
        final UserProfile sessionUser = sessionService.getUserById(request.getSession().getId());
        final UserProfile userGet = accountService.getUser(id);
        if (userGet != null && userGet.equals(sessionUser)) {
            return Response.status(Response.Status.OK).entity(userGet.toString()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }


    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyUser(UserProfile user, @PathParam("id") long id, @Context HttpServletRequest request){
        final AccountService accountService = (AccountService) context.get(AccountService.class);
        final SessionServiceImpl sessionService = (SessionServiceImpl) context.get(SessionServiceImpl.class);
        final UserProfile sessionUser = sessionService.getUserById(request.getSession().getId());
        final UserProfile modyfyedUser = accountService.getUser(id);
        logger.info("Users - post - modifyUser \"" + modyfyedUser.getLogin() + '"');

        if (modyfyedUser.equals(sessionUser)
                && user.getLogin().equals(modyfyedUser.getLogin())) {
            accountService.modifyUser(id, user);
            sessionService.deleteSession(request.getSession().getId());
            sessionService.newSession(request.getSession().getId(), user);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") long id, @Context HttpServletRequest request) {
        logger.info("Users - delete - deleteUser " + id);
        final AccountService accountService = (AccountService) context.get(AccountService.class);
        final SessionServiceImpl sessionService = (SessionServiceImpl) context.get(SessionServiceImpl.class);
        final UserProfile sessionUser = sessionService.getUserById(request.getSession().getId());
        final UserProfile deletingUser = accountService.getUser(id);

        if ((sessionUser != null)
                && (sessionUser.equals(deletingUser))) {
            accountService.deleteUser(id);
            sessionService.deleteSession(request.getSession().getId());
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }





    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        logger.info("Users - get - getAllUsers");
        final AccountService accountService = (AccountService) context.get(AccountService.class);
        final Collection<UserProfile> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new UserProfile[allUsers.size()])).build();
    }
}
