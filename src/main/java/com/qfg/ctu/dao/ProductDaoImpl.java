package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Product;
import com.qfg.ctu.util.Constant;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by rbtq on 7/26/16.
 */
public class ProductDaoImpl extends AbstractDao<Product> implements ProductDao {
    private Connection connection;
    private final String _tblName = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_PRODUCTS);
    private final String _sqlSelect = String.format("SELECT * FROM %s ", _tblName);

    public ProductDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product obj) throws SQLException {
        _update(connection, String.format("INSERT INTO %s (id, title, unitPrice, store) VALUES (?, ?, ?, ?)"),
                obj.getBarCode(), obj.getTitle(), obj.getUnitPrice(), obj.getStoreNum());
    }

    @Override
    public void update(Product obj) throws SQLException {
        _update(connection, String.format("UPDATE %s SET title=?, store=?, unitPrice=?", _tblName),
                obj.getTitle(), obj.getStoreNum(), obj.getUnitPrice());
    }

    @Override
    public void minusStore(Integer id, int count) throws SQLException {
        _update(connection, String.format("UPDATE %s SET store=store-? WHERE id=?", _tblName), count, id);
    }

    @Override
    public void delete(Integer id) throws SQLException {
        _update(connection, String.format("DELETE FROM %s WHERE id=?", _tblName), id);
    }

    @Override
    public Product findById(Integer id) throws SQLException {
        return _get(connection, _sqlSelect + "WHERE id=?", id);
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return _getArray(connection, _sqlSelect);
    }

    @Override
    public List<Product> findByQuery(String query, Object... args) throws SQLException {
        return _getArray(connection, _sqlSelect+query, args);
    }

    @Override
    protected Product _loadRecord(ResultSet qrs) throws SQLException {
        Product product = new Product();
        product.setBarCode(qrs.getInt("id"));
        product.setTitle(qrs.getString("title"));
        product.setUnitPrice(qrs.getInt("unitPrice"));
        product.setStoreNum(qrs.getInt("store"));

        return product;
    }
}
