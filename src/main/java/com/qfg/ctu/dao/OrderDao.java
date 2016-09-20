package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Order;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by rbtq on 7/30/16.
 */
public interface OrderDao extends GenericDao<Order, Integer> {

    List<Order> findAll(LocalDateTime from ,LocalDateTime to) throws SQLException;

    List<Order> findAll(LocalDateTime from ,LocalDateTime to, boolean desc) throws SQLException;

    Long getTotalPrice(LocalDateTime from ,LocalDateTime to) throws SQLException;
}
