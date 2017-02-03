package com.qfg.ctu.proxy;

import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.util.DbUtil;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.stream.Stream;

/**
 * Created by rbtq on 8/2/16.
 */
public class DBProxyHandler implements InvocationHandler {
    private Class<?> concreteClass;
    private final String company;

    public DBProxyHandler(Class<?> concreteClass){
        this(concreteClass, null);
    }

    public DBProxyHandler(Class<?> concreteClass, final String company){
        this.concreteClass = concreteClass;
        this.company = company;
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

        Method mtd;
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes == null || paramTypes.length == 0) {
            mtd = concreteClass.getDeclaredMethod(method.getName());
        } else {
            mtd = concreteClass.getDeclaredMethod(method.getName(), paramTypes);
        }

        if (null != mtd.getAnnotation(NeedDB.class)) {
            Connection conn = DbUtil.getConnection();
            if (null != company) {
                DbUtil.changeDb(conn, company);
            }

            setDbConn(obj, conn);
            try {
                conn.setAutoCommit(false);
                Object ret = method.invoke(obj, args);
                conn.commit();
                return ret;
            } catch (InvocationTargetException e) {
                DbUtil.rollbackQuietly(conn);
                Throwable t = e.getCause();
                throw (null == t) ? e : t;
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
