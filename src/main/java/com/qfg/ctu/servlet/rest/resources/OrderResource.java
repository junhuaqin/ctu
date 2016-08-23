package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestOrder;
import com.qfg.ctu.servlet.rest.services.OrderService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rbtq on 7/30/16.
 */
@Path("/orders")
public class OrderResource extends BaseResource{
    @Inject
    private OrderService orderService;

    @GET
    @Path("{from: \\d+}/{to: \\d+}")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("from") long from, @PathParam("to") long to) throws Exception {
        return Response.status(Response.Status.OK).entity(orderService.getAll(from, to)).build();
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(RestOrder restOrder) throws Exception {
        return Response.status(Response.Status.OK).entity(orderService.add(getAdminId(), restOrder)).build();
    }

    @GET
    @Path("statics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatics() throws Exception {
        return Response.status(Response.Status.OK).entity(orderService.getStatics()).build();
    }
}
