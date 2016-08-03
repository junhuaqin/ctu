package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestProduct;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

/**
 * Created by rbtq on 8/3/16.
 */
@Service(name=ProductServiceImpl.NAME)
public interface ProductService {
    List<RestProduct> getAll() throws Exception;
    RestProduct getByBarcode(Integer id) throws Exception;
    RestProduct getByQR(String id) throws Exception;
    RestProduct addProduct(RestProduct product) throws Exception;
    void decreaseStore(Integer id, Integer dec) throws Exception;
}
