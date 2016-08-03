package com.qfg.ctu.servlet.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;

/**
 * Created by rbtq on 7/28/16.
 */

@Provider
public class UnhandledExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(final Throwable t) {
        String errMsg;
        if (t instanceof SQLException) {
            SQLException se = (SQLException) t;
            errMsg = se.getMessage();
        }
        else if (t instanceof WebApplicationException) {
            WebApplicationException we = (WebApplicationException)t;
            return we.getResponse();
        }
        else {
            errMsg = "unknown error:" + t.getMessage();
        }

        t.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST).entity(errMsg).build();
    }
}
