package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.pojo.Order;
import com.qfg.ctu.servlet.rest.pojos.RestOrder;
import com.qfg.ctu.util.Constant;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.qfg.ctu.util.Constant.BEIJING_ZONE;

/**
 * Created by rbtq on 7/30/16.
 */
@Service
public class OrderService {
    private final static Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    @Inject
    private UserService userService;

    private RestOrder mapOrder2Rest(Order order) {
        RestOrder restOrder = new RestOrder();
        restOrder.id = order.getId();
        restOrder.totalPrice = order.getTotalPrice();
        restOrder.createdAt = order.getCreatedAt().toInstant(Constant.BEIJING_ZONE).toEpochMilli();
        try {
            restOrder.sale = userService.getById(order.getSale()).name;
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

    public List<RestOrder> getAll(long from, long to) throws Exception{
        List<Order> orders = DaoFactory.getOrderDao(null).findAll(
                LocalDateTime.ofEpochSecond(from/1000, 0, Constant.BEIJING_ZONE),
                LocalDateTime.ofEpochSecond(to/1000, 0, Constant.BEIJING_ZONE));

        return orders.stream().map(n->mapOrder2Rest(n)).collect(Collectors.toList());
    }
}
