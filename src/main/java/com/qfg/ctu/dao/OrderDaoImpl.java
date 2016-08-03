package com.qfg.ctu.dao;

import com.qfg.ctu.dao.pojo.Order;
import com.qfg.ctu.dao.pojo.OrderJoinItem;
import com.qfg.ctu.util.Constant;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 7/30/16.
 */
public class OrderDaoImpl extends AbstractDao<OrderJoinItem> implements OrderDao {
    private Connection connection;
    private final String _tblOrdersName = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_ORDERS);
    private final String _tblOrderItemName = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_ORDERITEMS);
    private final String _tblProducts = String.format("%s.%s", Constant.DB_CTU, Constant.TBL_PRODUCTS);
    private final String _sqlSelect = String.format("select a.*, b.*, c.title from %s a, %s b, %s c where a.id=b.order_id and b.product_id=c.id ", _tblOrdersName, _tblOrderItemName, _tblProducts);

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Order obj) throws SQLException {
        _update(connection, String.format("INSERT INTO %s (sale, totalPrice) VALUES (?, ?)", _tblOrdersName),
                obj.getSale(), obj.getTotalPrice());
        obj.setId(_getLastId(connection));
        for (Order.OrderItem item : obj.getItems()) {
            _update(connection, String.format("INSERT INTO %s (order_id, product_id, unitPrice, sale_count) VALUES (?,?,?,?)", _tblOrderItemName),
                    obj.getId(), item.getBarCode(), item.getUnitPrice(), item.getCount());
        }
    }

    @Override
    public void update(Order obj) throws SQLException {
        throw new SQLException("Can't update an order");
    }

    @Override
    public void delete(Integer id) throws SQLException {
        _update(connection, String.format("DELETE FROM %s WHERE id=?", _tblOrdersName), id);
    }

    private Order mapOrderJoinItem2Order(OrderJoinItem n) {
        Order order = new Order();
        order.setId(n.getId());
        order.setCreatedAt(n.getCreatedAt());
        order.setSale(n.getSale());

        Order.OrderItem item = new Order.OrderItem();
        item.setCount(n.getCount());
        item.setUnitPrice(n.getUnitPrice());
        item.setTitle(n.getTitle());
        item.setBarCode(n.getBarCode());
        order.addItem(item);

        return order;
    }

    @Override
    public Order findById(Integer id) throws SQLException {
        List<OrderJoinItem> orderJoinItems = _getArray(connection, _sqlSelect+ "AND id=?", id);
        if (orderJoinItems.isEmpty()) {
            return null;
        }

        Order order = mapOrderJoinItem2Order(orderJoinItems.get(0));
        for (OrderJoinItem item : orderJoinItems) {
            order.addItem(mapOrderJoinItem2Order(item).getItems().get(0));
        }

        return order;
    }

    @Override
    public List<Order> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Order> findAll(LocalDateTime from ,LocalDateTime to) throws SQLException {
        Map<Integer, Order> orderMap = new HashMap<>();
        List<OrderJoinItem> orderJoinItems = _getArray(connection, _sqlSelect);
        orderJoinItems.stream().map(this::mapOrderJoinItem2Order)
                                .forEach(n->{
            if (orderMap.containsKey(n.getId())) {
                orderMap.get(n.getId()).addItem(n.getItems().get(0));
            } else {
                orderMap.put(n.getId(), n);
            }
        });

        return orderMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Long getTotalPrice(LocalDateTime from, LocalDateTime to) throws SQLException {
        return _getLong(connection,
                        String.format("SELECT sum(totalPrice) FROM %s WHERE createdOn>=? AND createdOn <=?", _tblOrdersName),
                        from, to);
    }

    @Override
    public List<Order> findByQuery(String query, Object... args) throws SQLException {
        return null;
    }

    @Override
    protected OrderJoinItem _loadRecord(ResultSet qrs) throws SQLException {
        OrderJoinItem item = new OrderJoinItem();
        item.setId(qrs.getInt("id"));
        item.setSale(qrs.getInt("sale"));
        item.setTotalPrice(qrs.getInt("totalPrice"));
        item.setCreatedAt(LocalDateTime.ofEpochSecond(qrs.getTimestamp("createdOn").getTime()/1000, 0, Constant.BEIJING_ZONE));

        item.setBarCode(qrs.getInt("product_id"));
        item.setTitle(qrs.getString("title"));
        item.setUnitPrice(qrs.getInt("unitPrice"));
        item.setCount(qrs.getInt("sale_count"));
        return item;
    }
}
