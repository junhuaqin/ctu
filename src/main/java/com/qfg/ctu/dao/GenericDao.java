package com.qfg.ctu.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public interface GenericDao<T, I extends Serializable> {

    void save(T obj) throws SQLException;

    void update(T obj) throws SQLException;

    <E extends I> void delete(E id) throws SQLException;

    <E extends I> T findById(E id) throws SQLException;

    List<T> findAll() throws SQLException;

    List<T> findByQuery(String query, Object... args) throws SQLException;
}
