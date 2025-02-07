package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlTransient;

public class SearchedCustomerDtoJaxb extends CustomerDtoJaxb {
    @XmlTransient
    protected String address;
    @XmlTransient
    protected String phoneNumber;

    public SearchedCustomerDtoJaxb() {}
    public SearchedCustomerDtoJaxb(Long customerId, String name, String dni,
                                   String address, String phoneNumber) {
        super(customerId,name,dni,address,phoneNumber);

    }

    @Override
    public String toString() {
        return "CustomerDtoJaxb{" +
                "customerId=" + super.getCustomerId() +
                ", name='" + super.getName() + '\'' +
                ", dni='" + super.getDni() + '\'' +
                '}';
    }
}
