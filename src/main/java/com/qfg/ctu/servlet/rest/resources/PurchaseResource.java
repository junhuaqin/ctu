package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.exception.InvalidRequestException;
import com.qfg.ctu.servlet.rest.pojos.RestImportPurchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseConfirm;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseItem;
import com.qfg.ctu.servlet.rest.services.PurchaseService;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

/**
 * Created by rbtq on 9/20/16.
 */
@Path("/purchases")
public class PurchaseResource extends BaseResource {
    @Inject
    private PurchaseService purchaseService;

    @Context
    protected HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestPurchase> getAll() throws Exception {
        return purchaseService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}")
    public RestPurchase getById(@PathParam("id") int id) throws Exception {
        return purchaseService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/allItems")
    public List<RestPurchaseItem> getAllItems(@PathParam("id") int id) throws Exception {
        return purchaseService.getAllItems(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/allConfirms")
    public List<RestPurchaseConfirm> getAllConfirms(@PathParam("id") int id) throws Exception {
        return purchaseService.getAllConfirms(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestPurchase add(RestPurchase posted) throws Exception {
        return purchaseService.add(getAdminId(), posted);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("import")
    public RestImportPurchase importOrder() throws Exception {
        ServletFileUpload upload = new ServletFileUpload();
        String service = "";
        String document = "";
        FileItemIterator it = upload.getItemIterator(request);
        while (it.hasNext()) {
            FileItemStream item = it.next();
            String name = item.getFieldName();
            InputStream stream = item.openStream();
            if (item.isFormField()) {
                if (name.equalsIgnoreCase("service")) {
                    service = Streams.asString(stream);
                }
            }
            else {
                if (name.equalsIgnoreCase("file")) {
                    document = Streams.asString(stream);
                }
            }
        }

        if (!service.equalsIgnoreCase("tbh")) {
            throw new InvalidRequestException(Response.Status.BAD_REQUEST, "Don't support " + service);
        } else if (document.isEmpty()) {
            throw new InvalidRequestException(Response.Status.BAD_REQUEST, "Please upload document");
        }

        return purchaseService.importTBHOrder(document);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/addItem")
    public RestPurchaseItem addItem(@PathParam("id") int id, RestPurchaseItem posted) throws Exception {
        return purchaseService.addItem(id, posted);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/confirm")
    public RestPurchaseConfirm confirmItem(@PathParam("id") int id, RestPurchaseConfirm posted) throws Exception {
        return purchaseService.confirm(getAdminId(), id, posted);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/putItem")
    public RestPurchaseItem putItem(@PathParam("id") int id, RestPurchaseItem posted) throws Exception {
        return purchaseService.updateItem(id, posted);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/putConfirm")
    public RestPurchaseConfirm putConfirm(@PathParam("id") int id, RestPurchaseConfirm posted) throws Exception {
        return purchaseService.updateConfirm(getAdminId(), id, posted);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/deleteItem/{itemId: \\d+}")
    public Boolean deleteItem(@PathParam("id") int id, @PathParam("itemId") int itemId) throws Exception {
        return purchaseService.deleteItem(id, itemId);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}/deleteConfirm/{confirmId: \\d+}")
    public Boolean deleteConfirm(@PathParam("id") int id, @PathParam("id") int confirmId) throws Exception {
        return purchaseService.deleteConfirm(id, confirmId);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}")
    public Boolean delete(@PathParam("id") int id) throws Exception {
        return purchaseService.deletePurchase(id);
    }
}
