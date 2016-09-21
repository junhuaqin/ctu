package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestPurchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseConfirm;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseItem;
import com.qfg.ctu.servlet.rest.services.PurchaseService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rbtq on 9/20/16.
 */
@Path("/purchases")
public class PurchaseResource extends BaseResource {
    @Inject
    private PurchaseService purchaseService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.getAll()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/allItems")
    public Response getAllItems(@PathParam("id") int id) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.getAllItems(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/allConfirms")
    public Response getAllConfirms(@PathParam("id") int id) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.getAllConfirms(id)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response add(RestPurchase posted) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.add(getAdminId(), posted)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/addItem")
    public Response addItem(@PathParam("id") int id, RestPurchaseItem posted) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.addItem(id, posted)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/confirm")
    public Response confirmItem(@PathParam("id") int id, RestPurchaseConfirm posted) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.confirm(getAdminId(), id, posted)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/putItem")
    public Response putItem(@PathParam("id") int id, RestPurchaseItem posted) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.updateItem(id, posted)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/putConfirm")
    public Response putConfirm(@PathParam("id") int id, RestPurchaseConfirm posted) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.updateConfirm(getAdminId(), id, posted)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/deleteItem/{itemId: \\d+}")
    public Response deleteItem(@PathParam("id") int id, @PathParam("itemId") int itemId) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.deleteItem(id, itemId)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/deleteConfirm/{confirmId: \\d+}")
    public Response deleteConfirm(@PathParam("id") int id, @PathParam("id") int confirmId) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.deleteConfirm(id, confirmId)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") int id) throws Exception {
        return Response.status(Response.Status.OK).entity(purchaseService.deletePurchase(id)).build();
    }
}
