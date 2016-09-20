package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Purchase;

import java.sql.SQLException;

/**
 * Created by rbtq on 9/20/16.
 */
public interface PurchaseDao extends GenericDao<Purchase, Integer> {
    void addTotalPrice(int id, int price) throws SQLException;
}
