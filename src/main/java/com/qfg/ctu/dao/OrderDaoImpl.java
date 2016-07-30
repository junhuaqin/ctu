package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Order;
import com.qfg.ctu.util.Constant;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbtq on 7/30/16.
 */
public class OrderDaoImpl implements OrderDao {
    private Connection connection;

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Order obj) {

    }

    @Override
    public void update(Order obj) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Order findById(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public List<Order> findAll(LocalDateTime from ,LocalDateTime to) {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(1);
        order.setSale(1);
        order.setCreatedAt(LocalDateTime.now(Constant.BEIJING_ZONE));
        order.setTotalPrice(12300);

        List<Order.OrderItem> items = new ArrayList<>();
        Order.OrderItem product = new Order.OrderItem();
        product.setCount(1);
        product.setTitle("test");
        product.setUnitPrice(12300);
        items.add(product);

        order.setItems(items);
        orders.add(order);

        return orders;
    }

    @Override
    public List<Order> findByQuery(String query, Object... args) {
        return null;
    }
}
