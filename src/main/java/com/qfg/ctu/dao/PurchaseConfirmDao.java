package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Purchase;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 9/20/16.
 */
public interface PurchaseConfirmDao {
    void save(Integer pid, Purchase.PurchaseItem.Confirm obj) throws SQLException;

    void update(Purchase.PurchaseItem.Confirm obj) throws SQLException;

    void delete(Integer id) throws SQLException;

    Purchase.PurchaseItem.Confirm findById(Integer id) throws SQLException;

    List<Purchase.PurchaseItem.Confirm> findByPId(Integer id) throws SQLException;
}
