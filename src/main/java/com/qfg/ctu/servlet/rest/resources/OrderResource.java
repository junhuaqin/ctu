package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.services.OrderService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rbtq on 7/30/16.
 */
@Path("/orders")
public class OrderResource {
    @Inject
    private OrderService orderService;

    @GET
    @Path("{from: \\d+}/{to: \\d+}")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("from") long from, @PathParam("to") long to) throws Exception {
        return Response.status(Response.Status.OK).entity(orderService.getAll(from, to)).build();
    }
}
