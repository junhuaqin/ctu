package com.qfg.ctu.servlet.rest.resources;

import com.qfg.ctu.servlet.rest.services.factory.AOPServiceFactory;
import com.qfg.ctu.servlet.rest.services.factory.ServiceFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

/**
 * Created by rbtq on 8/23/16.
 */
public class BaseResource {
    public static final String HEADER_PARAM_USER_ID = "AuthorizedUser";
    public static final String HEADER_PARAM_COMPANY = "ctuCompany";

    @Context
    protected HttpHeaders headers;

    public int getAdminId() {
        int adminId = 0;
        String userIdStr = headers.getHeaderString(HEADER_PARAM_USER_ID);
        if (userIdStr != null) {
            adminId = Integer.parseInt(userIdStr);
        }
        return adminId;
    }

    public String getCompany() {
        return headers.getHeaderString(HEADER_PARAM_COMPANY);
    }

    public ServiceFactory getServiceFactory() {
        return new AOPServiceFactory(getCompany());
    }
}
