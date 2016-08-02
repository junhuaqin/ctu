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
}
