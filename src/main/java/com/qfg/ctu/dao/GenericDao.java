package com.qfg.ctu.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public interface GenericDao<T, I extends Serializable> {

    void save(T obj);

    void update(T obj);

    <E extends I> void delete(E id);

    <E extends I> T findById(E id);

    List<T> findAll();

    List<T> findByQuery(String query, Object... args);
}
