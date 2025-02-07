package es.udc.rs.telco.jaxrs.exceptions;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.rs.telco.jaxrs.dto.InstanceNotFoundExceptionDtoJaxb;

@Provider
public class InstanceNotFoundExceptionMapper implements ExceptionMapper<InstanceNotFoundException> {

    @Override
    public Response toResponse(InstanceNotFoundException ex) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new InstanceNotFoundExceptionDtoJaxb(
                        ex.getInstanceId(), ex.getInstanceType())).build();

    }
}
