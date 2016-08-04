package com.qfg.ctu.proxy;

import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.util.DbUtil;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rbtq on 8/2/16.
 */
public class DBProxyHandler implements InvocationHandler {
    private Class concreteClass;

    public DBProxyHandler(Class concreteClass){
        this.concreteClass = concreteClass;
    }

    private void setDbConn(Object obj, Connection conn) {
        Stream<Field> methods = Stream.of(concreteClass.getDeclaredFields());
        methods.filter(n -> n.getAnnotation(Inject.class) != null)
                .filter(n -> n.getType().equals(Connection.class))
                .forEach(n -> {
            try {
                n.setAccessible(true);
                n.set(obj, conn);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj = concreteClass.newInstance();

        List<Class> classes = (args == null) ? new ArrayList<>()
                            : Stream.of(args).map(Object::getClass).collect(Collectors.toList());
        Method mtd = concreteClass.getDeclaredMethod(method.getName(), classes.toArray(new Class[classes.size()]));

        if (null != mtd.getAnnotation(NeedDB.class)) {
            Connection conn = DbUtil.getConnection();
            setDbConn(obj, conn);
            try {
                conn.setAutoCommit(false);
                Object ret = method.invoke(obj, args);
                conn.commit();
                return ret;
            } catch (Exception e) {
                DbUtil.rollbackQuietly(conn);
                throw e;
            } finally {
                DbUtil.closeQuietly(conn);
            }
        } else {
            return method.invoke(obj, args);
        }
    }
}
