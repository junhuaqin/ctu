package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public interface ProductDao extends GenericDao<Product, Integer> {
    @Override
    void save(Product obj) throws SQLException;

    @Override
    void update(Product obj) throws SQLException;

    void minusStore(Integer id, int count) throws SQLException;

    @Override
    void delete(Integer id) throws SQLException;

    @Override
    Product findById(Integer id) throws SQLException;

    @Override
    List<Product> findAll() throws SQLException;

    @Override
    List<Product> findByQuery(String query, Object... args) throws SQLException;
}
