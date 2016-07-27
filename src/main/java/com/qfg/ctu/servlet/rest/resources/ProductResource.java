package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.exception.InvalidRequestException;
import com.qfg.ctu.servlet.rest.services.ProductService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws InvalidRequestException {
        return Response.status(Response.Status.OK).entity(productService.getAll()).build();
    }
}
