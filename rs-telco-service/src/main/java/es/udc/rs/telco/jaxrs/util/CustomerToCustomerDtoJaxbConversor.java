package es.udc.rs.telco.jaxrs.util;

import java.util.ArrayList;
import java.util.List;

import es.udc.rs.telco.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.SearchedCustomerDtoJaxb;
import es.udc.rs.telco.model.customer.Customer;
//import es.udc.ws.util.


public class CustomerToCustomerDtoJaxbConversor {

    public static List<CustomerDtoJaxb> toCustomerDtoJaxb(List<Customer> customer) {
        List<CustomerDtoJaxb> customerDtos = new ArrayList<>(customer.size());
        for (Customer cust : customer) {
            customerDtos.add(toCustomerDtoJaxb(cust));
        }
        return customerDtos;
    }

    public static CustomerDtoJaxb toCustomerDtoJaxb(Customer cust) {
        return new CustomerDtoJaxb(cust.getCustomerId(), cust.getName(), cust.getDni(),
                cust.getAddress(), cust.getPhoneNumber());
    }

    public static Customer toCustomer(CustomerDtoJaxb cust) {
        if (cust == null) {
            throw new IllegalArgumentException("customerDto cannot be null");
        }
        return new Customer(cust.getCustomerId(), cust.getName(), cust.getDni(), cust.getAddress(), cust.getPhoneNumber());
    }


    public static List<SearchedCustomerDtoJaxb> toSearchedCustomerDtoJaxb(List<Customer> customer) {
        List<SearchedCustomerDtoJaxb> customerDtos = new ArrayList<>(customer.size());
        for (Customer cust : customer) {
            customerDtos.add(toSearchedCustomerDtoJaxb(cust));
        }
        return customerDtos;
    }

    public static SearchedCustomerDtoJaxb toSearchedCustomerDtoJaxb(Customer cust) {
        return new SearchedCustomerDtoJaxb(cust.getCustomerId(), cust.getName(), cust.getDni(), cust.getAddress(), cust.getPhoneNumber());
    }
}

