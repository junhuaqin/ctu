package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Product;

import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public interface ProductDao extends GenericDao<Product, Integer> {
    @Override
    void save(Product obj);

    @Override
    void update(Product obj);

    @Override
    void delete(Integer id);

    @Override
    Product findById(Integer id);

    @Override
    List<Product> findAll();

    @Override
    List<Product> findByQuery(String query, Object... args);
}
