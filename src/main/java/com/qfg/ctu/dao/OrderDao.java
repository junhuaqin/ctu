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
    void save(Order obj) throws SQLException;

    @Override
    void update(Order obj) throws SQLException;

    @Override
    void delete(Integer id) throws SQLException;

    @Override
    Order findById(Integer id) throws SQLException;

    @Override
    List<Order> findAll() throws SQLException;

    List<Order> findAll(LocalDateTime from ,LocalDateTime to) throws SQLException;

    Long getTotalPrice(LocalDateTime from ,LocalDateTime to) throws SQLException;

    @Override
    List<Order> findByQuery(String query, Object... args) throws SQLException;
}
