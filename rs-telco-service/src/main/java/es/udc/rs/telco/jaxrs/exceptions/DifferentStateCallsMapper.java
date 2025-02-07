package es.udc.rs.telco.jaxrs.exceptions;

import es.udc.rs.telco.jaxrs.dto.OtherExceptionsDtoJaxb;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import es.udc.rs.telco.model.telcoservice.exceptions.DifferentStateCalls;

@Provider
public class DifferentStateCallsMapper implements ExceptionMapper<DifferentStateCalls> {

    @Override
    public Response toResponse(DifferentStateCalls ex) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new OtherExceptionsDtoJaxb("DifferentStateCalls", ex.getMessage()))
                .build();
    }
}
