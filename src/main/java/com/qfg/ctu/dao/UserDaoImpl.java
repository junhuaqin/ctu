package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public class UserDaoImpl implements UserDao {
    private Connection connection;
    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User obj) throws SQLException {

    }

    @Override
    public void update(User obj) throws SQLException {

    }

    @Override
    public void delete(Integer id) throws SQLException {

    }

    @Override
    public User findById(Integer id) throws SQLException {
        User user = new User();
        user.setId(id);
        user.setName("sq");
        return user;
    }

    @Override
    public List<User> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<User> findByQuery(String query, Object... args) throws SQLException {
        return null;
    }
}
