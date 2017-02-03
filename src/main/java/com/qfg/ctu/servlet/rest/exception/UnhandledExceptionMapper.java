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
        Response.StatusType status = Response.Status.BAD_REQUEST;
        Throwable throwable = t;
        if (null != t.getCause())
            throwable = t.getCause();
        if (throwable instanceof SQLException) {
            SQLException se = (SQLException) throwable;
            errMsg = se.getMessage();
        }
        else if (throwable instanceof WebApplicationException) {
            WebApplicationException we = (WebApplicationException)throwable;
            status = we.getResponse().getStatusInfo();
            errMsg = we.getMessage();
        }
        else if (throwable instanceof InvalidRequestException){
            InvalidRequestException e = (InvalidRequestException)throwable;
            status = e.getStatusCode();
            errMsg = e.getMessage();
        }
        else {
            errMsg = "unknown error:" + throwable.getMessage();
        }

        t.printStackTrace();
        return Response.status(status).entity(new ErrMsg(errMsg, status)).build();
    }

    private static class ErrMsg {
        private String err;
        private int status;

        public ErrMsg(String msg, Response.StatusType status) {
            this.err = msg;
            this.status = status.getStatusCode();
        }

        public String getErr() {
            return err;
        }
        public int getStatus() {
            return status;
        }
    }
}
