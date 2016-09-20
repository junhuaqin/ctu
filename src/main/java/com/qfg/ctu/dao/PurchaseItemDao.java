package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Purchase;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 9/20/16.
 */
public interface PurchaseItemDao {
    void save(Integer pid, Purchase.PurchaseItem obj) throws SQLException;

    void update(Purchase.PurchaseItem obj) throws SQLException;

    void delete(Integer id) throws SQLException;

    Purchase.PurchaseItem findById(Integer id) throws SQLException;

    List<Purchase.PurchaseItem> findByPId(Integer id) throws SQLException;

    void confirm(Integer id, Integer amount) throws SQLException;
}
