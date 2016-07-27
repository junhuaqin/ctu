package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.ProductDao;
import com.qfg.ctu.dao.pojo.Product;
import com.qfg.ctu.servlet.rest.pojos.RestProduct;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 7/26/16.
 */

@Service
public class ProductService {
    public List<RestProduct> getAll() {
        ProductDao productDao = DaoFactory.getProductDao(null);
        List<Product> products = productDao.findAll();
        List restProducts = products.stream().map(n->new RestProduct(n)).collect(Collectors.toList());
        return restProducts;
    }
}
