package com.qfg.ctu.servlet.rest.filter;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rbtq on 8/4/16.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    private final static Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());

    @Context
    HttpServletRequest realRequest;

    @Override
    public void filter(ContainerRequestContext request) throws WebApplicationException {
        LOGGER.log(Level.INFO, "auth filter");
    }
}