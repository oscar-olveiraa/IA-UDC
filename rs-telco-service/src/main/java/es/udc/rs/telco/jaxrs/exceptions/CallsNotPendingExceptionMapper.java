package es.udc.rs.telco.jaxrs.exceptions;

import es.udc.rs.telco.jaxrs.dto.OtherExceptionsDtoJaxb;
import es.udc.rs.telco.model.telcoservice.exceptions.CallsNotPendingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CallsNotPendingExceptionMapper implements ExceptionMapper<CallsNotPendingException> {

    @Override
    public Response toResponse(CallsNotPendingException ex) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new OtherExceptionsDtoJaxb("CallsNotPending", ex.getMessage()))
                .build();
    }
}