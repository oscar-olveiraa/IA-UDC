package es.udc.rs.telco.jaxrs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


@XmlRootElement(name = "customer")
@XmlType(name="customerJaxbType", propOrder = {"customerId", "name", "dni", "address", "phoneNumber"})
public class CustomerDtoJaxb {
    @XmlAttribute(name = "customerId", required = true)
    private Long customerId;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String dni;
    @XmlElement(required = false)
    protected String address;
    @XmlElement(required = true)
    protected String phoneNumber;

    public CustomerDtoJaxb() {
    }

    public CustomerDtoJaxb(Long customerId, String name, String dni,
                             String address, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.dni = dni;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }

    @Schema(description = "Nombre del cliente", allowableValues =  {}, required=true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(description = "ID del cliente", allowableValues =  {}, required=true)
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Schema(description = "DNI del cliente", allowableValues =  {}, required=true)
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Schema(description = "Dirección del cliente", allowableValues =  {}, required=true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Schema(description = "Número de teléfono del cliente", allowableValues =  {}, required=true)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "CustomerDtoJaxb{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", dni='" + dni + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
