package com.example.tg;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResponseFactory {
    public static WebApplicationException generateBadRequestException(String message) {
        return new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                .entity(message)
                .type(MediaType.TEXT_PLAIN).build());
    }

    public static NotAuthorizedException generateNotAuthorizedException() {
        return new NotAuthorizedException(Response.Status.UNAUTHORIZED);
    }

    public static Response generateNotFoundResponse(String message) {
        return Response.status(Response.Status.NOT_FOUND).entity(message)
                .type(MediaType.TEXT_PLAIN).build();
    }

    public static Response generateOkNoContentResponse() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
