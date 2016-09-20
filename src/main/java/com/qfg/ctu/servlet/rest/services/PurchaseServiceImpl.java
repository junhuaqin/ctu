package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.pojo.Purchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchase;
import com.qfg.ctu.util.DateTimeUtil;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by rbtq on 9/20/16.
 */
public class PurchaseServiceImpl implements PurchaseService {
    public final static String NAME = "com.qfg.ctu.servlet.rest.services.PurchaseServiceImpl";
    @Inject
    Connection conn;

    @NeedDB
    @Override
    public RestPurchase add(RestPurchase purchase) throws Exception {
        Purchase innerPurchase = purchase.toInner();
        innerPurchase.setCreatedAt(DateTimeUtil.nowInBeiJing());

        DaoFactory.getPurchaseDao(conn).save(innerPurchase);
        innerPurchase.getItems().forEach(n -> {
            try {
                DaoFactory.getPurchaseItemDao(conn).save(innerPurchase.getId(), n);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        purchase.id = innerPurchase.getId();

        return purchase;
    }
}
