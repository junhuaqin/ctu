package com.qfg.ctu.proxy;

import com.qfg.ctu.annotations.ConnectAcceptor;
import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.util.DbUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.stream.Stream;

/**
 * Created by rbtq on 8/2/16.
 */
public class DBProxyHandler implements InvocationHandler {
    private Object concreteClass;

    public DBProxyHandler(Object concreteClass){
        this.concreteClass=concreteClass;
    }

    private void setDbConn(Connection conn) {
        Stream<Method> methods = Stream.of(concreteClass.getClass().getMethods());
        methods.filter(n -> n.getAnnotation(ConnectAcceptor.class) != null).findFirst().ifPresent(n -> {
            try {
                n.invoke(concreteClass, conn);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (null != method.getAnnotation(NeedDB.class)) {
            Connection conn = DbUtil.atomicGetConnection();
            setDbConn(conn);
            try {
                return method.invoke(concreteClass, args);
            } catch (Exception e) {
                DbUtil.rollbackQuietly(conn);
                throw e;
            } finally {
                DbUtil.closeQuietly(conn);
            }
        } else {
            return method.invoke(concreteClass, args);
        }
    }
}
