package com.qfg.ctu.servlet;

import com.qfg.ctu.util.DbUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rbtq on 7/23/16.
 */

public class Listener implements ServletContextListener {

    private final static Logger LOGGER = Logger.getLogger(Listener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.log(Level.INFO, "contextInitialized");
        try {
            DbUtil.initConnectionPoolByJndi();
            if (!DbUtil.doesDbExist("ctu")) {
                LOGGER.log(Level.WARNING, "Database doesn't exist, rebuild it");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.log(Level.INFO, "contextDestroyed");
    }
}
