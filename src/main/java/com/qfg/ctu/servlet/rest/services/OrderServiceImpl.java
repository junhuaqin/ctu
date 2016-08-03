package com.qfg.ctu.servlet.rest.services;

import com.google.gson.Gson;
import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.OrderDao;
import com.qfg.ctu.dao.ProductDao;
import com.qfg.ctu.dao.pojo.Order;
import com.qfg.ctu.servlet.rest.pojos.RestOrder;
import com.qfg.ctu.servlet.rest.pojos.RestStatics;
import com.qfg.ctu.util.Constant;

import javax.inject.Inject;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 8/3/16.
 */
public class OrderServiceImpl implements OrderService {
    public final static String NAME = "com.qfg.ctu.servlet.rest.services.OrderServiceImpl";
    private final static Logger LOGGER = Logger.getLogger(OrderServiceImpl.class.getName());

    @Inject
    Connection conn;

    private RestOrder mapOrder2Rest(Order order) {
        RestOrder restOrder = new RestOrder();
        restOrder.id = order.getId();
        restOrder.totalPrice = order.getTotalPrice();
        restOrder.createdAt = order.getCreatedAt().toInstant(Constant.BEIJING_ZONE).toEpochMilli();
        try {
            restOrder.sale = DaoFactory.getUserDao(null).findById(order.getSale()).getName();
        } catch (Exception e) {
            e.printStackTrace();
            restOrder.sale = "unknown";
        }

        restOrder.items = order.getItems().stream().map(n->{
            RestOrder.RestOrderItem item = new RestOrder.RestOrderItem();
            item.barCode = n.getBarCode();
            item.title = n.getTitle();
            item.count = n.getCount();
            item.unitPrice = n.getUnitPrice();
            return item;
        }).collect(Collectors.toList());

        return restOrder;
    }

    @NeedDB
    public List<RestOrder> getAll(Long from, Long to) throws Exception {
        List<Order> orders = DaoFactory.getOrderDao(conn).findAll(
                LocalDateTime.ofEpochSecond(from/1000, 0, Constant.BEIJING_ZONE),
                LocalDateTime.ofEpochSecond(to/1000, 0, Constant.BEIJING_ZONE));

        return orders.stream().map(this::mapOrder2Rest).collect(Collectors.toList());
    }

    @NeedDB
    public RestStatics getStatics() throws Exception {
        List<Order> ordersInToday = DaoFactory.getOrderDao(conn).findAll(
                LocalDateTime.now(Constant.BEIJING_ZONE).withHour(0).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now(Constant.BEIJING_ZONE)
        );

        List<Order> ordersInThisMonth = DaoFactory.getOrderDao(conn).findAll(
                LocalDateTime.now(Constant.BEIJING_ZONE).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now(Constant.BEIJING_ZONE)
        );

        RestStatics statics = new RestStatics();
        ordersInToday.forEach(n->statics.curDay+=n.getTotalPrice());
        ordersInThisMonth.forEach(n->statics.curMonth+=n.getTotalPrice());

        return statics;
    }

    @NeedDB
    public RestOrder add(RestOrder restOrder) throws Exception {
        Order order = new Order();
        order.setSale(1);
        order.setTotalPrice(restOrder.totalPrice);
        order.setCreatedAt(LocalDateTime.now(Constant.BEIJING_ZONE));
        order.setItems(restOrder.items.stream().map(n->{
            Order.OrderItem item = new Order.OrderItem();
            item.setTitle(n.title);
            item.setUnitPrice(n.unitPrice);
            item.setCount(n.count);
            return item;
        }).collect(Collectors.toList()));

        LOGGER.log(Level.INFO, "new order:"+(new Gson()).toJson(order));

        OrderDao orderDao = DaoFactory.getOrderDao(conn);
        orderDao.save(order);

        ProductDao productDao = DaoFactory.getProductDao(conn);
        for (RestOrder.RestOrderItem item : restOrder.items) {
            productDao.minusStore(item.barCode, item.count);
        }

        restOrder.createdAt = order.getCreatedAt().toInstant(Constant.BEIJING_ZONE).toEpochMilli();

        return restOrder;
    }
}