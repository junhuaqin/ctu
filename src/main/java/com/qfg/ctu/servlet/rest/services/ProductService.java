package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestProduct;
import org.jvnet.hk2.annotations.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Created by rbtq on 8/3/16.
 */
@Service(name=ProductServiceImpl.NAME)
public interface ProductService {
    List<RestProduct> getAll() throws Exception;
    RestProduct getByBarcode(String id) throws Exception;
    RestProduct getByQR(String id) throws Exception;
    RestProduct addProduct(RestProduct product) throws Exception;
    RestProduct updateProduct(RestProduct product) throws Exception;
    RestProduct deleteProduct(String id) throws Exception;
    void decreaseStore(String id, Integer dec) throws Exception;
    void importProducts(InputStream is) throws Exception;
}
