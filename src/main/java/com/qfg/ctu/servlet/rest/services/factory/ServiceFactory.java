package com.qfg.ctu.servlet.rest.services.factory;

import com.qfg.ctu.servlet.rest.services.OrderService;
import com.qfg.ctu.servlet.rest.services.ProductService;
import com.qfg.ctu.servlet.rest.services.PurchaseService;
import com.qfg.ctu.servlet.rest.services.UserService;

/**
 * Created by Robert Qin on 03/02/2017.
 */
public interface ServiceFactory {
    UserService getUserService();
    OrderService getOrderService();
    ProductService getProductService();
    PurchaseService getPurchaseService();
}
