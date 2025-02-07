package es.udc.rs.telco.client.service.rest.json;

import com.fasterxml.jackson.annotation.JsonValue;

public interface JAXBElementMixin<T> {
    @JsonValue
    Object getValue();
}
