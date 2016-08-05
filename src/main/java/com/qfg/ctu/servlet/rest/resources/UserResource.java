package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestUser;
import com.qfg.ctu.servlet.rest.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rbtq on 8/3/16.
 */
@Path("/users")
public class UserResource {
    private static final String _LOGIN_SUB_URL = "login";
    public static final String LOGIN_PATH = "/users/" + _LOGIN_SUB_URL;

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws Exception {
        return Response.status(Response.Status.OK).entity(userService.getAll()).build();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) throws Exception {
        return Response.status(Response.Status.OK).entity(userService.getById(id)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(_LOGIN_SUB_URL)
    public Response login(RestUser posted) throws Exception {
        return Response.status(Response.Status.OK).entity(userService.login(posted.userName, posted.password)).build();
    }
}
