package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.User;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by rbtq on 7/26/16.
 */
public interface UserDao extends GenericDao<User, Integer>  {

    User findByUserName(String name) throws SQLException;

    void setLastLogin(Integer id, LocalDateTime time) throws SQLException;
}
