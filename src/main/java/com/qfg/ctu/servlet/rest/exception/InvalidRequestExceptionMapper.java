package com.qfg.ctu.servlet.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException> {
	public static final String ERROR_MESSAGE = "This request was no good.";

	@Override
	public Response toResponse(final InvalidRequestException e) {
		e.printStackTrace();
		return Response.status(e.getStatusCode()).entity(e.getMessage()).build();
	}
}
