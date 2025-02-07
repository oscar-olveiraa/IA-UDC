package es.udc.rs.telco.client.service.util;

import es.udc.rs.telco.client.service.rest.dto.InputValidationExceptionJaxbType;
import es.udc.rs.telco.client.service.rest.dto.InstanceNotFoundExceptionJaxbType;
import es.udc.rs.telco.client.service.rest.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JaxbExceptionConversor {

    public static InputValidationException toInputValidationException(
            InputValidationExceptionJaxbType exDto) {
        return new InputValidationException(exDto.getMessage());
    }

    public static InstanceNotFoundException toInstanceNotFoundException(
            InstanceNotFoundExceptionJaxbType exDto) {
        return new InstanceNotFoundException(exDto.getInstanceId(),
                exDto.getInstanceType());
    }
}
