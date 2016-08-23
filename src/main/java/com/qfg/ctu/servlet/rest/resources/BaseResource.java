package com.qfg.ctu.servlet.rest.resources;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

/**
 * Created by rbtq on 8/23/16.
 */
public class BaseResource {
    public static final String HEADER_PARAM_USER_ID = "AuthorizedUser";

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
}
