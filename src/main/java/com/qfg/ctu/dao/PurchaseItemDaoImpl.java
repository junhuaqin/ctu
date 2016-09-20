package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Purchase;
import com.qfg.ctu.util.Constant;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 9/20/16.
 */
public class PurchaseItemDaoImpl extends AbstractDao<Purchase.PurchaseItem> implements PurchaseItemDao {
    private Connection connection;
    private final String _tblPurchaseItemName = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_PURCHASE_ITEMS);
    private final String _tblProducts = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_PRODUCTS);

    private final String _sqlSelect = String.format("SELECT a.*, b.title FROM %s a, %s b WHERE a.product_id=b.id ",
            _tblPurchaseItemName, _tblProducts);

    public PurchaseItemDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Integer pid, Purchase.PurchaseItem obj) throws SQLException {
        _update(connection, String.format("INSERT INTO %s (purchase_id, product_id, unitPrice, amount) VALUES (?,?,?,?)", _tblPurchaseItemName),
                pid, obj.getBarCode(), obj.getUnitPrice(), obj.getAmount());
    }

    @Override
    public void update(Purchase.PurchaseItem obj) throws SQLException {
        _update(connection, String.format("UPDATE %s SET unitPrice=?, amount=? WHERE id=?", _tblPurchaseItemName),
                obj.getUnitPrice(), obj.getAmount(), obj.getId());
    }

    @Override
    public void delete(Integer id) throws SQLException {
        _update(connection, String.format("DELETE FROM %s WHERE id=?", _tblPurchaseItemName), id);
    }

    @Override
    public Purchase.PurchaseItem findById(Integer id) throws SQLException {
        return _get(connection, _sqlSelect + "WHERE id=?", id);
    }

    @Override
    public List<Purchase.PurchaseItem> findByPId(Integer id) throws SQLException {
        return _getArray(connection, _sqlSelect + "WHERE purchase_id=?", id);
    }

    @Override
    protected Purchase.PurchaseItem _loadRecord(ResultSet qrs) throws SQLException {
        Purchase.PurchaseItem item = new Purchase.PurchaseItem();
        item.setId(qrs.getInt("id"));
        item.setBarCode(qrs.getString("product_id"));
        item.setTitle(qrs.getString("title"));
        item.setUnitPrice(qrs.getInt("unitPrice"));
        item.setAmount(qrs.getInt("amount"));
        item.setAmountConfirmed(qrs.getInt("amountConfirmed"));
        return item;
    }
}
