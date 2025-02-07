package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name="inputValidationException")
@XmlType(name="inputValidationExceptionJaxbType")
public class InputValidationExceptionDtoJaxb {

    @XmlElement(required = true)
    private String message;

    public InputValidationExceptionDtoJaxb(){}

    public InputValidationExceptionDtoJaxb(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "InputValidationExceptionDtoJaxb{" +
                "message='" + message + '\'' +
                '}';
    }

}
