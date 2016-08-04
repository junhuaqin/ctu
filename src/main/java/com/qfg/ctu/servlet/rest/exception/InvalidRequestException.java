package com.qfg.ctu.servlet.rest.exception;

import javax.ws.rs.core.Response;

public class InvalidRequestException extends Exception {
	private static final long serialVersionUID = 1L;
	private Response.Status statusCode = Response.Status.BAD_REQUEST;

	public InvalidRequestException(String message){
		super(message);
	}
	public InvalidRequestException(Response.Status code, String message) {
		super(message);
		this.statusCode = code;
	}

	public Response.Status getStatusCode() {
		return this.statusCode;
	}
}
