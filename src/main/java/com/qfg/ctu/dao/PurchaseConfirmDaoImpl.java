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
public class PurchaseConfirmDaoImpl extends AbstractDao<Purchase.PurchaseItem.Confirm> implements PurchaseConfirmDao {
    private Connection connection;
    private final String _tblPurchaseConfirmName = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_PURCHASE_CONFIRMS);
    private final String _sqlSelect = String.format("SELECT * FROM %s ", _tblPurchaseConfirmName);

    public PurchaseConfirmDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Integer pid, Purchase.PurchaseItem.Confirm obj) throws SQLException {
        _update(connection, String.format("INSERT INTO %s (purchase_item_id, amount, sale, confirmOn) VALUES (?,?,?,?)", _tblPurchaseConfirmName),
                pid, obj.getAmount(), obj.getSale(), obj.getConfirmAt());
    }

    @Override
    public void update(Purchase.PurchaseItem.Confirm obj) throws SQLException {
        _update(connection, String.format("UPDATE %s SET amount=?, confirmOn=? WHERE id=?", _tblPurchaseConfirmName),
                obj.getAmount(), obj.getConfirmAt(), obj.getId());
    }

    @Override
    public void delete(Integer id) throws SQLException {
        _update(connection, String.format("DELETE FROM %s WHERE id=?", _tblPurchaseConfirmName), id);
    }

    @Override
    public Purchase.PurchaseItem.Confirm findById(Integer id) throws SQLException {
        return _get(connection, _sqlSelect + "WHERE id=?", id);
    }

    @Override
    public List<Purchase.PurchaseItem.Confirm> findByPId(Integer id) throws SQLException {
        return _getArray(connection, _sqlSelect + "WHERE purchase_item_id=?", id);
    }

    @Override
    protected Purchase.PurchaseItem.Confirm _loadRecord(ResultSet qrs) throws SQLException {
        Purchase.PurchaseItem.Confirm confirm = new Purchase.PurchaseItem.Confirm();
        confirm.setId(qrs.getInt("id"));
        confirm.setAmount(qrs.getInt("amount"));
        confirm.setSale(qrs.getInt("sale"));
        confirm.setConfirmAt(DateTimeUtil.mapTimestamp2LocalDateTime(qrs.getTimestamp("confirmOn")));
        return confirm;
    }
}
