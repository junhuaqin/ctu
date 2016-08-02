package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestProduct;
import com.qfg.ctu.servlet.rest.services.ProductService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by rbtq on 7/26/16.
 */
@Path("/products")
public class ProductResource {
    private final static Logger LOGGER = Logger.getLogger(ProductResource.class.getName());

    @Inject
    private ProductService productService;

    @GET
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws Exception {
        return Response.status(Response.Status.OK).entity(productService.getAll()).build();
    }

    @GET
    @Path("{barCode: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBarcode(@PathParam("barCode") int id) throws Exception {
        return Response.status(Response.Status.OK).entity(productService.getByBarcode(id)).build();
    }

    @GET
    @Path("qr/{barCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByQR(@PathParam("barCode") String id) throws Exception {
        return Response.status(Response.Status.OK).entity(productService.getByQR(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(@NotNull RestProduct product) throws Exception {
        return Response.status(Response.Status.OK).entity(productService.addProduct(product)).build();
    }
}
