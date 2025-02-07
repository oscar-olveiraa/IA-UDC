package es.udc.rs.telco.client.service;

import es.udc.rs.telco.client.service.rest.exceptions.ClientCustomerHasCallsException;
import es.udc.rs.telco.client.service.rest.exceptions.ClientDifferentStateCallsException;
import es.udc.rs.telco.client.service.rest.exceptions.ClientMonthNotExpired;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientTelcoService {

    Long addCustomer(ClientCustomerDto newCustomer) throws InputValidationException;

    Long addPhoneCall(ClientPhoneCallDto call) throws InputValidationException, InstanceNotFoundException;

    void deleteClient(Long customerId) throws InstanceNotFoundException, ClientCustomerHasCallsException;

    void changeCallStatus(Long customerId, int month, int year, String newStatus) throws InputValidationException, InstanceNotFoundException, ClientMonthNotExpired, ClientDifferentStateCallsException;

    List<ClientPhoneCallDto> searchCallsByDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate, int start, int maxResults, String type) throws InputValidationException, InstanceNotFoundException;
}
