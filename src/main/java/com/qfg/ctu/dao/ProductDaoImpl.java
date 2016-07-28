package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public class ProductDaoImpl implements ProductDao {
    private Connection connection;

    public ProductDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product obj) {

    }

    @Override
    public void update(Product obj) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Product findById(Integer id) throws SQLException {
        Product product = new Product();
        product.setBarCode(id);
        product.setTitle("test"+id);
        product.setUnitPrice(12350);
        product.setStoreNum(100);
        if (0 == id) {
            throw new SQLException("test");
        }
        return product;
    }

    @Override
    public List<Product> findAll() {
        Product product = new Product();
        product.setBarCode(12345);
        product.setTitle("test");
        product.setUnitPrice(12300);
        product.setStoreNum(100);
        return Arrays.asList(product);
    }

    @Override
    public List<Product> findByQuery(String query, Object... args) {
        return null;
    }
}
