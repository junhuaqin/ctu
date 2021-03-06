package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestOrder;
import com.qfg.ctu.servlet.rest.pojos.RestStatics;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rbtq on 7/30/16.
 */
@Path("/orders")
public class OrderResource extends BaseResource{

    @GET
    @Path("{from: \\d+}/{to: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestOrder> getAll(@PathParam("from") long from, @PathParam("to") long to) throws Exception {
        return getServiceFactory().getOrderService().getAll(from, to);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestOrder add(RestOrder restOrder) throws Exception {
        return getServiceFactory().getOrderService().add(getAdminId(), restOrder);
    }

    @GET
    @Path("statics")
    @Produces(MediaType.APPLICATION_JSON)
    public RestStatics getStatics() throws Exception {
        return getServiceFactory().getOrderService().getStatics();
    }
}
