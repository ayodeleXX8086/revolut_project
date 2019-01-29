package com.revolut.restapi.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by ayomide on 1/28/2019.
 */
@Provider
public class RestExceptionMapper implements ExceptionMapper<RestException> {
    @Override
    public Response toResponse(RestException exception) {
        return Response.status(exception.getCode()).entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN).build();
    }
}
