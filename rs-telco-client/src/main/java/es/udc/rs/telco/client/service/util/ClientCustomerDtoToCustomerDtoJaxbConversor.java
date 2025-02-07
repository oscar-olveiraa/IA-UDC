package es.udc.rs.telco.client.service.util;

import es.udc.rs.telco.client.service.ClientCustomerDto;
import es.udc.rs.telco.client.service.rest.dto.CustomerJaxbType;
import es.udc.rs.telco.client.service.rest.dto.ObjectFactory;
import jakarta.xml.bind.JAXBElement;

import java.util.ArrayList;
import java.util.List;

public class ClientCustomerDtoToCustomerDtoJaxbConversor {

    public static JAXBElement<CustomerJaxbType> toJaxbCustomer(ClientCustomerDto customerDto) {
        CustomerJaxbType customerJaxb = new CustomerJaxbType();
        customerJaxb.setCustomerId(customerDto.getCustomerId() != null ? customerDto.getCustomerId() : -1);
        customerJaxb.setAddress(customerDto.getAddress());
        customerJaxb.setDni(customerDto.getDni());
        customerJaxb.setPhoneNumber(customerDto.getPhoneNumber());
        customerJaxb.setName(customerDto.getName());
        return new ObjectFactory().createCustomer(customerJaxb);
    }

    public static ClientCustomerDto toCustomerDto(CustomerJaxbType customerJaxb) {
        return new ClientCustomerDto(customerJaxb.getCustomerId(),customerJaxb.getName(), customerJaxb.getDni(), customerJaxb.getAddress(), customerJaxb.getPhoneNumber());
    }

    public static List<ClientCustomerDto> toCustomerDtos(List<CustomerJaxbType> customerListDto) {
        List<ClientCustomerDto> CustomerDtos = new ArrayList<>(customerListDto.size());
        for (CustomerJaxbType CustomerDtoJaxb : customerListDto) {
            CustomerDtos.add(toCustomerDto(CustomerDtoJaxb));
        }
        return CustomerDtos;
    }
}
