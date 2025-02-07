package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name="applicationException")
@XmlType(name="applicationExceptionJaxbType")
public class OtherExceptionsDtoJaxb {

    @XmlAttribute(required = true)
    private String errorType;
    @XmlElement(required = true)
    private String message;

    public OtherExceptionsDtoJaxb(){}

    public OtherExceptionsDtoJaxb(String errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApplicationExceptionDtoJaxb{" +
                "errorType='" + errorType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}

