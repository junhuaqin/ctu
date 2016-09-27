package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestChangePassword;
import com.qfg.ctu.servlet.rest.pojos.RestUser;
import com.qfg.ctu.servlet.rest.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rbtq on 8/3/16.
 */
@Path("/users")
public class UserResource extends BaseResource{
    private static final String _LOGIN_SUB_URL = "login";
    public static final String LOGIN_PATH = "/users/" + _LOGIN_SUB_URL;

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestUser> getAll() throws Exception {
        return userService.getAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestUser getById(@PathParam("id") int id) throws Exception {
        return userService.getById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(_LOGIN_SUB_URL)
    public RestUser login(RestUser posted) throws Exception {
        return userService.login(posted.userName, posted.password);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestUser add(RestUser posted) throws Exception {
        return userService.add(posted);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("changePassword")
    public RestUser changePassword(RestChangePassword posted) throws Exception {
        return userService.changePassword(posted);
    }
}
