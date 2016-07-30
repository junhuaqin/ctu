package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Order;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by rbtq on 7/30/16.
 */
public interface OrderDao extends GenericDao<Order, Integer> {
    @Override
    void save(Order obj);

    @Override
    void update(Order obj);

    @Override
    void delete(Integer id);

    @Override
    Order findById(Integer id) throws SQLException;

    @Override
    List<Order> findAll();

    List<Order> findAll(LocalDateTime from ,LocalDateTime to);

    @Override
    List<Order> findByQuery(String query, Object... args);
}
