package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Product;

import java.sql.SQLException;

/**
 * Created by rbtq on 7/26/16.
 */
public interface ProductDao extends GenericDao<Product, String> {

    void minusStore(String id, int count) throws SQLException;
}
