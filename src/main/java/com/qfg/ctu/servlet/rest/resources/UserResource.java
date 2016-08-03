package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rbtq on 8/3/16.
 */
@Path("/users")
public class UserResource {
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
    public Response getByBarcode(@PathParam("id") int id) throws Exception {
        return Response.status(Response.Status.OK).entity(userService.getById(id)).build();
    }
}
