package es.udc.rs.telco.jaxrs.exceptions;

import es.udc.rs.telco.jaxrs.dto.OtherExceptionsDtoJaxb;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import es.udc.rs.telco.model.telcoservice.exceptions.MonthNotExpiredException;

@Provider
public class MonthNotExpiredExceptionMapper implements ExceptionMapper<MonthNotExpiredException> {

    @Override
    public Response toResponse(MonthNotExpiredException ex) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new OtherExceptionsDtoJaxb("MonthNotExpired", ex.getMessage()))
                .build();
    }
}
