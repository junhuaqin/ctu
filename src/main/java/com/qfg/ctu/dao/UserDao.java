package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.User;

import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public interface UserDao extends GenericDao<User, Integer>  {

    @Override
    void save(User obj);

    @Override
    void update(User obj);

    @Override
    void delete(Integer id);

    @Override
    User findById(Integer id);

    @Override
    List<User> findAll();

    @Override
    List<User> findByQuery(String query, Object... args);
}
