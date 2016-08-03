package com.qfg.ctu.servlet.rest.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.ProductDao;
import com.qfg.ctu.dao.pojo.Product;
import com.qfg.ctu.servlet.rest.pojos.RestProduct;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 7/26/16.
 */

public class ProductServiceImpl implements ProductService {
    public final static String NAME = "com.qfg.ctu.servlet.rest.services.ProductServiceImpl";
    private final static Logger LOGGER = Logger.getLogger(ProductServiceImpl.class.getName());
    private static String TBH = "http://www.tbh.cn/member_api/product.php";

    @Inject
    private Connection conn;

    @NeedDB
    public List<RestProduct> getAll() throws Exception {
        ProductDao productDao = DaoFactory.getProductDao(conn);
        List<Product> products = productDao.findAll();
        return products.stream().map(RestProduct::new).collect(Collectors.toList());
    }

    @NeedDB
    public RestProduct getByBarcode(Integer id) throws Exception {
        ProductDao productDao = DaoFactory.getProductDao(conn);
        Product product = productDao.findById(id);

        if (null == product) {
            return null;
        }

        return new RestProduct(product);
    }

    private int getBarcodeFromTBH(String id) throws Exception {
        LOGGER.log(Level.INFO, String.format("Query QR:%s", id));

        Form form = (new Form())
                .param("act", "get_product_by_unique_code")
                .param("unique_code", id);

        Optional<Client> client = Optional.empty();
        Optional<Response> response = Optional.empty();
        try {
            client = Optional.of(ClientBuilder.newClient());
            WebTarget target = client.get().target(TBH);
            response = Optional.of(target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED)));

            if (response.get().getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + response.get().getStatus());
            }

            String result = response.get().readEntity(String.class);
            JsonObject jsonObject = (new JsonParser()).parse(result).getAsJsonObject();
            JsonObject info = jsonObject.getAsJsonObject("info");
            return info.get("product_code").getAsInt();
        } finally {
            response.ifPresent(Response::close);
            client.ifPresent(Client::close);
        }
    }

    @NeedDB
    public RestProduct getByQR(String id) throws Exception {
        return getByBarcode(getBarcodeFromTBH(id));
    }

    @NeedDB
    public RestProduct addProduct(RestProduct product) throws Exception {
        DaoFactory.getProductDao(conn).save(product.toInner());
        return product;
    }

    @NeedDB
    public void decreaseStore(Integer id, Integer dec) throws Exception {
        DaoFactory.getProductDao(conn).minusStore(id, dec);
    }
}
