package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public interface UserDao extends GenericDao<User, Integer>  {

    @Override
    void save(User obj) throws SQLException;

    @Override
    void update(User obj) throws SQLException;

    @Override
    void delete(Integer id) throws SQLException;

    @Override
    User findById(Integer id) throws SQLException;

    User findByUserName(String name) throws SQLException;

    void setLastLogin(Integer id, LocalDateTime time) throws SQLException;

    @Override
    List<User> findAll() throws SQLException;

    @Override
    List<User> findByQuery(String query, Object... args) throws SQLException;
}
