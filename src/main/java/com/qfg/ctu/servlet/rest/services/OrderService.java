package com.qfg.ctu.servlet.rest.services;

import com.google.gson.Gson;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.OrderDao;
import com.qfg.ctu.dao.ProductDao;
import com.qfg.ctu.dao.pojo.Order;
import com.qfg.ctu.servlet.rest.pojos.RestOrder;
import com.qfg.ctu.servlet.rest.pojos.RestStatics;
import com.qfg.ctu.util.Constant;
import org.jvnet.hk2.annotations.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 7/30/16.
 */
@Service
public class OrderService {
    private final static Logger LOGGER = Logger.getLogger(OrderService.class.getName());

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
            item.title = n.getTitle();
            item.count = n.getCount();
            item.unitPrice = n.getUnitPrice();
            return item;
        }).collect(Collectors.toList());

        return restOrder;
    }

    public List<RestOrder> getAll(long from, long to) throws Exception {
        List<Order> orders = DaoFactory.getOrderDao(null).findAll(
                LocalDateTime.ofEpochSecond(from/1000, 0, Constant.BEIJING_ZONE),
                LocalDateTime.ofEpochSecond(to/1000, 0, Constant.BEIJING_ZONE));

        return orders.stream().map(this::mapOrder2Rest).collect(Collectors.toList());
    }

    public RestStatics getStatics() throws Exception {
        RestStatics statics = new RestStatics();
        statics.curMonth = 7500000;
        statics.curDay = 540000;
        return statics;
    }

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

        OrderDao orderDao = DaoFactory.getOrderDao(null);
        orderDao.save(order);

        ProductDao productDao = DaoFactory.getProductDao(null);
        for (RestOrder.RestOrderItem item : restOrder.items) {
            productDao.minusStore(item.barCode, item.count);
        }

        restOrder.createdAt = order.getCreatedAt().toInstant(Constant.BEIJING_ZONE).toEpochMilli();

        return restOrder;
    }
}
