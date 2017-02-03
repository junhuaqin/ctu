package com.qfg.ctu.servlet.rest.services.factory;

import com.qfg.ctu.proxy.DBProxyHandler;
import com.qfg.ctu.servlet.rest.services.OrderService;
import com.qfg.ctu.servlet.rest.services.ProductService;
import com.qfg.ctu.servlet.rest.services.PurchaseService;
import com.qfg.ctu.servlet.rest.services.UserService;
import org.jvnet.hk2.annotations.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Robert Qin on 03/02/2017.
 */
public class AOPServiceFactory implements ServiceFactory {
    private final static Logger LOGGER = Logger.getLogger(AOPServiceFactory.class.getName());
    private final String company;
    public AOPServiceFactory(final String company) {
        this.company = company;
    }

    @Override
    public UserService getUserService() {
        return getProxyService(UserService.class);
    }

    @Override
    public OrderService getOrderService() {
        return getProxyService(OrderService.class);
    }

    @Override
    public ProductService getProductService() {
        return getProxyService(ProductService.class);
    }

    @Override
    public PurchaseService getPurchaseService() {
        return getProxyService(PurchaseService.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T getProxyService(Class<T> n) {
        Service service = n.getAnnotation(Service.class);
        Class c;
        try {
            c = n.getClassLoader().loadClass(service.name());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, String.format("failed to load class:%s", service.name()));
            return null;
        }

        InvocationHandler ih= new DBProxyHandler(c, company);//代理实例的调用处理程序。
        return (T) Proxy.newProxyInstance(c.getClassLoader(), c.getInterfaces(), ih);
    }
}
