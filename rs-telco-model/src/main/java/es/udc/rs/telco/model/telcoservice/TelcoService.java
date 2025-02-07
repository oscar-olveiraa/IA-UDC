package es.udc.rs.telco.model.telcoservice;

import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.telcoservice.exceptions.CustomerHasCallsException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.rs.telco.model.phonecall.*;
import es.udc.rs.telco.model.telcoservice.exceptions.CallsNotPendingException;
import es.udc.rs.telco.model.telcoservice.exceptions.DifferentStateCalls;
import es.udc.rs.telco.model.telcoservice.exceptions.MonthNotExpiredException;

import java.time.LocalDateTime;
import java.util.List;

public interface TelcoService {

    //Crear un cliente
    public Customer addCustomer(Customer c) throws InputValidationException;

    //Actualizar datos de un cliente
    public void updateCustomer(Customer c) throws InstanceNotFoundException, InputValidationException;

    //Borrar un cliente
    public void deleteCustomer(Long customerid) throws InstanceNotFoundException, CustomerHasCallsException;

    //Añadir llamadas ligadas a un cliente
    public PhoneCall addPhoneCall(PhoneCall p) throws InputValidationException, InstanceNotFoundException;

    //Obtener Llamadas de un Cliente en un Mes/Año Específico
    public List<PhoneCall> getCallsByCustomerAndMonth(Long CustomerId, int year,int month) throws InstanceNotFoundException, CallsNotPendingException, MonthNotExpiredException;

    //Cambiar Estado de las Llamadas de un Mes
    public void updateCallStatus(Long CustomerId, int year,int month, PhoneCallStatus newStatus) throws InstanceNotFoundException, DifferentStateCalls, MonthNotExpiredException;

    //Buscar Llamadas por Intervalo de Fechas
    public List<PhoneCall> getCallsByDateRange(Long CustomerId, LocalDateTime start_date, LocalDateTime end_date, PhoneCallType type, int start, int maxResults) throws InstanceNotFoundException;

    //Buscar cliente por DNI
    Customer getCustomerByDNI(String dni) throws InstanceNotFoundException;

    //Buscar cliente por ID
    Customer getCustomerById(Long customerId) throws InstanceNotFoundException;

    //Buscar cliente por texto
    List<Customer> searchCustomersByName(String text, int start, int maxResults) throws InputValidationException;

    //public List<PhoneCall> getCallsByCustomerAndMonthDEBUG(Long CustomerId, int year, int month);

    //public void cleanDatabase();
}
