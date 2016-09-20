package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestPurchase;
import com.qfg.ctu.servlet.rest.services.PurchaseService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rbtq on 9/20/16.
 */
public class PurchaseResource extends BaseResource {
    @Inject
    private PurchaseService purchaseService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response add(RestPurchase posted) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.add(posted)).build();
    }
}
