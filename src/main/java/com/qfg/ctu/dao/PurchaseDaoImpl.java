package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Purchase;
import com.qfg.ctu.util.Constant;
import com.qfg.ctu.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 9/20/16.
 */
public class PurchaseDaoImpl extends AbstractDao<Purchase> implements PurchaseDao {
    private Connection connection;
    private final String _tblPurchasesName = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_PURCHASES);
    private final String _sqlSelect = String.format("SELECT * FROM %s ", _tblPurchasesName);

    public PurchaseDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Purchase obj) throws SQLException {
        _update(connection, String.format("INSERT INTO %s (sale, totalPrice, createdOn) VALUES (?,?,?)", _tblPurchasesName),
                obj.getSale(), obj.getTotalPrice(), obj.getCreatedAt());
        obj.setId(_getLastId(connection));
    }

    @Override
    public void update(Purchase obj) throws SQLException {
        _update(connection, String.format("UPDATE %s SET totalPrice=? WHERE id=?", _tblPurchasesName),
                obj.getTotalPrice(), obj.getId());
    }

    @Override
    public void delete(Integer id) throws SQLException {
        _update(connection, String.format("DELETE FROM %s WHERE id=?", _tblPurchasesName), id);
    }

    @Override
    public Purchase findById(Integer id) throws SQLException {
        return _get(connection, _sqlSelect + "WHERE id=?", id);
    }

    @Override
    public List<Purchase> findAll() throws SQLException {
        return _getArray(connection, _sqlSelect);
    }

    @Override
    public List<Purchase> findByQuery(String query, Object... args) throws SQLException {
        return _getArray(connection, _sqlSelect + query, args);
    }

    @Override
    protected Purchase _loadRecord(ResultSet qrs) throws SQLException {
        Purchase purchase = new Purchase();
        purchase.setId(qrs.getInt("id"));
        purchase.setSale(qrs.getInt("sale"));
        purchase.setCreatedAt(DateTimeUtil.mapTimestamp2LocalDateTime(qrs.getTimestamp("createdOn")));
        purchase.setTotalPrice(qrs.getInt("totalPrice"));
        return purchase;
    }

    @Override
    public void addTotalPrice(int id, int price) throws SQLException {
        _update(connection, String.format("UPDATE %s SET totalPrice=totalPrice+? WHERE id=?", _tblPurchasesName),
                price, id);
    }
}
