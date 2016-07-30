package com.qfg.ctu.servlet.rest.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.ProductDao;
import com.qfg.ctu.dao.pojo.Product;
import com.qfg.ctu.servlet.rest.pojos.RestProduct;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 7/26/16.
 */

@Service
public class ProductService {
    private final static Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private static String TBH = "http://www.tbh.cn/member_api/product.php";

    public List<RestProduct> getAll() {
        ProductDao productDao = DaoFactory.getProductDao(null);
        List<Product> products = productDao.findAll();
        return products.stream().map(RestProduct::new).collect(Collectors.toList());
    }

    public RestProduct getByBarcode(int id) throws Exception {
        ProductDao productDao = DaoFactory.getProductDao(null);
        return new RestProduct(productDao.findById(id));
    }

    private int getBarcodeFromTBH(String id) throws Exception {
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

    public RestProduct getByQR(String id) throws Exception {
        return getByBarcode(getBarcodeFromTBH(id));
    }

    public RestProduct addProduct(RestProduct product) throws Exception {
        DaoFactory.getProductDao(null).save(product.toInner());
        return product;
    }
}
