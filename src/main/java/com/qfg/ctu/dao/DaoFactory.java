package com.qfg.ctu.dao;

import java.sql.Connection;

/**
 * Created by rbtq on 7/26/16.
 */
public class DaoFactory {
    public static ProductDao getProductDao(Connection connection) {
        return new ProductDaoImpl(connection);
    }

    public static OrderDao getOrderDao(Connection connection) {
        return new OrderDaoImpl(connection);
    }

    public static UserDao getUserDao(Connection connection) {
        return new UserDaoImpl(connection);
    }

    public static PurchaseDao getPurchaseDao(Connection connection) {
        return new PurchaseDaoImpl(connection);
    }

    public static PurchaseItemDao getPurchaseItemDao(Connection connection) {
        return new PurchaseItemDaoImpl(connection);
    }

    public static PurchaseConfirmDao getPurchaseConfirmDao(Connection connection) {
        return new PurchaseConfirmDaoImpl(connection);
    }
}
