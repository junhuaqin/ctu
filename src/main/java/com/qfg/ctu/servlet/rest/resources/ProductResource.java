package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.pojos.RestProduct;
import com.qfg.ctu.servlet.rest.services.ProductService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rbtq on 7/26/16.
 */
@Path("/products")
public class ProductResource extends BaseResource{
    private final static Logger LOGGER = Logger.getLogger(ProductResource.class.getName());

    @Inject
    private ProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestProduct> getAll() throws Exception {
        return productService.getAll();
    }

    @GET
    @Path("{barCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestProduct getByBarcode(@PathParam("barCode") String id) throws Exception {
        return productService.getByBarcode(id);
    }

    @GET
    @Path("qr/{qrCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestProduct getByQR(@PathParam("qrCode") String id) throws Exception {
        return productService.getByQR(id);
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestProduct addProduct(@NotNull RestProduct product) throws Exception {
        return productService.addProduct(product);
    }

    @PUT
    @Path("{barCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestProduct updateProduct(@PathParam("barCode") String id, @NotNull RestProduct product) throws Exception {
        product.barCode = id;
        return productService.updateProduct(product);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{barCode}")
    public Boolean deleteProduct(@PathParam("barCode") String id) throws Exception {
        return productService.deleteProduct(id);
    }
}
