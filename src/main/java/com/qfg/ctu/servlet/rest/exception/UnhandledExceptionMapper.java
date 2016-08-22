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
        Throwable throwable = t;
        if (null != t.getCause())
            throwable = t.getCause();
        if (throwable instanceof SQLException) {
            SQLException se = (SQLException) throwable;
            errMsg = se.getMessage();
        }
        else if (throwable instanceof WebApplicationException) {
            WebApplicationException we = (WebApplicationException)throwable;
            return we.getResponse();
        }
        else if (throwable instanceof InvalidRequestException){
            InvalidRequestException e = (InvalidRequestException)throwable;
            return Response.status(e.getStatusCode()).entity(e.getMessage()).build();
        }
        else {
            errMsg = "unknown error:" + throwable.getMessage();
        }

        t.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST).entity(errMsg).build();
    }
}
