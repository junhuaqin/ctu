package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.User;
import com.qfg.ctu.util.Constant;
import com.qfg.ctu.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private Connection connection;
    private final String _tblName = String.format("%s", Constant.TBL_ACCOUNTS);
    private final String _sqlSelect = String.format("SELECT * FROM %s ", _tblName);

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User obj) throws SQLException {
        _update(connection, String.format("INSERT INTO %s (name, username, password, token, createdOn, role_id) VALUES (?,?,?,?,?,?)", _tblName),
                obj.getName(), obj.getUserName(), obj.getPassword(), obj.getToken(), obj.getCreatedAt(), obj.getRole());
        obj.setId(_getLastId(connection));
    }

    @Override
    public void update(User obj) throws SQLException {
        throw new SQLException("Not support to update user");
    }

    @Override
    public void delete(Integer id) throws SQLException {
        _update(connection, String.format("DELETE FROM %s WHERE id=?", _tblName), id);
    }

    @Override
    public User findById(Integer id) throws SQLException {
        return _get(connection, _sqlSelect + "WHERE id=?", id);
    }

    @Override
    public User findByUserName(String name) throws SQLException {
        return _get(connection, _sqlSelect + "WHERE username=?", name);
    }

    @Override
    public void setLastLogin(Integer id, LocalDateTime time) throws SQLException {
        _update(connection, String.format("UPDATE %s SET lastLoginOn=? WHERE id=?", _tblName), time, id);
    }

    @Override
    public void changePassword(Integer id, String password) throws SQLException {
        _update(connection, String.format("UPDATE %s SET password=? WHERE id=?", _tblName), password, id);
    }

    @Override
    public List<User> findAll() throws SQLException {
        return _getArray(connection, _sqlSelect);
    }

    @Override
    public List<User> findByQuery(String query, Object... args) throws SQLException {
        return _getArray(connection, _sqlSelect + query, args);
    }

    @Override
    protected User _loadRecord(ResultSet qrs) throws SQLException {
        User user = new User();
        user.setId(qrs.getInt("id"));
        user.setName(qrs.getString("name"));
        user.setUserName(qrs.getString("username"));
        user.setPassword(qrs.getString("password"));
        user.setCreatedAt(DateTimeUtil.mapTimestamp2LocalDateTime(qrs.getTimestamp("createdOn")));
        user.setLastLoginAt(DateTimeUtil.mapTimestamp2LocalDateTime(qrs.getTimestamp("lastLoginOn")));
        user.setToken(qrs.getString("token"));
        user.setActive(qrs.getByte("active")>0);
        user.setRole(qrs.getInt("role_id"));
        return user;
    }
}
