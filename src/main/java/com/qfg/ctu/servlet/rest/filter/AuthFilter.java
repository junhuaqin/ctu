package com.qfg.ctu.servlet.rest.filter;

import com.qfg.ctu.servlet.rest.pojos.RestUser;
import com.qfg.ctu.servlet.rest.resources.BaseResource;
import com.qfg.ctu.servlet.rest.resources.UserResource;
import com.qfg.ctu.servlet.rest.services.factory.AOPServiceFactory;
import com.qfg.ctu.util.Constant;
import com.qfg.ctu.util.StringUtil;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
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

    @Override
    public void filter(ContainerRequestContext request) throws WebApplicationException {
        String company = request.getHeaderString(BaseResource.HEADER_PARAM_COMPANY);
        if (StringUtil.isNullOrEmpty(company)) {
            throw new WebApplicationException("please enter company", Response.Status.UNAUTHORIZED);
        }

        if (_needAuth(request)) {
            String auth = request.getHeaderString(Constant.AUTH);
            if (StringUtil.isNullOrEmpty(auth)) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            auth = auth.trim();
            String[] lap = auth.split(":", 2);
            if (null == lap || lap.length != 2) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            } else {
                try {
                    RestUser user = new AOPServiceFactory(company).getUserService().login(lap[0], lap[1]);
                    request.getHeaders().add(BaseResource.HEADER_PARAM_USER_ID, user.id + "");
                } catch (Exception e) {
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
            }
        }
    }

    private boolean _needAuth(ContainerRequestContext request) {

        String path = request.getUriInfo().getPath();
        //some version of jersey not include "/"
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.equalsIgnoreCase(UserResource.LOGIN_PATH)) {
            LOGGER.log(Level.INFO, String.format("resource do not need check authentication, path=%s", path));
            return false;
        }

        return true;
    }
}