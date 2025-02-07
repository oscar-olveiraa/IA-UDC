package es.udc.rs.telco.model.telcoservice;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.telcoservice.exceptions.CustomerHasCallsException;
import es.udc.rs.telco.model.telcoservice.exceptions.DifferentStateCalls;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import es.udc.rs.telco.model.telcoservice.exceptions.CallsNotPendingException;
import es.udc.rs.telco.model.telcoservice.exceptions.MonthNotExpiredException;
import es.udc.ws.util.validation.PropertyValidator;

import java.time.temporal.ChronoUnit;

public class MockTelcoService implements TelcoService {

    private Map<Long, Customer> clientsMap = new LinkedHashMap<>();
    private Map<Long, PhoneCall> phoneCallsMap = new LinkedHashMap<>();
    private Map<Long, List<PhoneCall>> phoneCallsByUserMap = new LinkedHashMap<>();

    private long lastClientId = 0;
    private long lastPhoneCallId = 0;

    private synchronized long getNextClientId() {
        return ++lastClientId;
    }

    private synchronized long getNextPhoneCallId() {
        return ++lastPhoneCallId;
    }

    //Metodo auxiliar
    private static void validateMonthExpired(LocalDateTime firstDate, LocalDateTime secondDate) throws MonthNotExpiredException {
        if (ChronoUnit.MONTHS.between(firstDate, secondDate) <= 0) throw new MonthNotExpiredException();
    }

    //Metodo auxiliar
    private void validateCustomerExists(Long CustomerId) throws InstanceNotFoundException {
        if (!clientsMap.containsKey(CustomerId))
            throw new InstanceNotFoundException(CustomerId, Customer.class.getSimpleName());
    }

    //Metodo auxiliar
    private List<PhoneCall> getCallsByUser(Long CustomerId) throws InstanceNotFoundException {
        validateCustomerExists(CustomerId);
        if (phoneCallsByUserMap.containsKey(CustomerId)) {
            return phoneCallsByUserMap.get(CustomerId);
        } else {
            return List.of();
        }
    }

    //Metodo auxiliar para añadir clientes
    private static void validateCustomer(Customer c) throws InputValidationException {
        if (c == null) {
            throw new InputValidationException("Customer can't be null.");
        }

            PropertyValidator.validateMandatoryString("name", c.getName());
            PropertyValidator.validateMandatoryString("address", c.getAddress());
            validateDni(c.getDni());
            validatePhoneNumber(c.getPhoneNumber());

    }

    //Metodo auxiliar para validar DNI usuario
    private static void validateDni(String dni) throws InputValidationException {
        if (dni == null || dni.length() != 9 || !Character.isLetter(dni.charAt(8))) {
            throw new InputValidationException("The DNI must have 8 numbers and 1 letter.");
        }

        String numbers = dni.substring(0, 8);
        for (char c : numbers.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new InputValidationException("The DNI must begin with 8 numbers.");
            }
        }
    }

    //Metodo auxiliar para validar teléfono usuario
    private static void validatePhoneNumber(String phoneNumber) throws InputValidationException {
        if (phoneNumber == null || phoneNumber.length() != 9) {
            throw new InputValidationException("The phone number must be 9 digits.");
        }

        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);
            if (!Character.isDigit(c)) {
                throw new InputValidationException("The phone number can only contain digits ");
            }
        }
    }

    //Metodo auxiliar para validar llamadas
    private static void validatePhoneCall(PhoneCall phoneCall) throws InputValidationException {
        if (phoneCall == null) {
            throw new InputValidationException("Phone call can't be null.");
        }

        if (phoneCall.getStartDate() == null) {
            throw new InputValidationException("Start date can't be null.");
        }

        if (phoneCall.getDuration() == null || phoneCall.getDuration() <= 0) {
            throw new InputValidationException("Duration must be greater than 0.");
        }

        validatePhoneNumber(phoneCall.getDestinationNumber());

        PhoneCallType type = phoneCall.getPhoneCallType();

        if (phoneCall.getPhoneCallType() == null || type != PhoneCallType.LOCAL && type != PhoneCallType.INTERNATIONAL && type != PhoneCallType.NATIONAL) {
            throw new InputValidationException("Phone call type is not correct");
        }
    }


    @Override
    public List<PhoneCall> getCallsByCustomerAndMonth(Long CustomerId, int year, int month) throws InstanceNotFoundException, CallsNotPendingException, MonthNotExpiredException {
        validateMonthExpired(LocalDateTime.of(year, month, 1, 0, 0, 0, 0), LocalDateTime.now());

        List<PhoneCall> customerCalls = getCallsByUser(CustomerId);
        List<PhoneCall> validPhoneCalls = new java.util.ArrayList<>(List.of());
        for (PhoneCall phoneCall : customerCalls) {
            if (phoneCall.getStartDate().getMonth().getValue() == month && phoneCall.getStartDate().getYear() == year) {
                if (phoneCall.getPhoneCallStatus() != PhoneCallStatus.PENDING)
                    throw new CallsNotPendingException(year, Month.of(month));
                else
                    validPhoneCalls.add(phoneCall);

            }
        }
        return validPhoneCalls;
    }

    @Override
    public void updateCallStatus(Long CustomerId, int year, int month, PhoneCallStatus newStatus) throws InstanceNotFoundException, DifferentStateCalls, MonthNotExpiredException {
        validateMonthExpired(LocalDateTime.of(year, month, 1, 0, 0, 0, 0), LocalDateTime.now());
        List<PhoneCall> phoneCallList = getCallsByUser(CustomerId);
        for (PhoneCall phoneCall : phoneCallList) {
            if (phoneCall.getStartDate().getMonth().getValue() == month
                    && phoneCall.getStartDate().getYear() == year) {
                if (newStatus == PhoneCallStatus.PENDING && phoneCall.getPhoneCallStatus() != PhoneCallStatus.PENDING) {throw new DifferentStateCalls();}
                if (newStatus == PhoneCallStatus.BILLED && (phoneCall.getPhoneCallStatus() != PhoneCallStatus.PENDING && phoneCall.getPhoneCallStatus() != PhoneCallStatus.BILLED)) {throw new DifferentStateCalls();}
                if (newStatus == PhoneCallStatus.PAID && (phoneCall.getPhoneCallStatus() != PhoneCallStatus.BILLED && phoneCall.getPhoneCallStatus() != PhoneCallStatus.PAID)) {throw new DifferentStateCalls();}
            }
        }
        for (PhoneCall phoneCall : phoneCallList) {
            if (phoneCall.getStartDate().getMonth().getValue() == month
                    && phoneCall.getStartDate().getYear() == year) {
                switch (newStatus) {
                    case BILLED: {
                        if (phoneCall.getPhoneCallStatus() == PhoneCallStatus.PENDING) {
                            phoneCall.setPhoneCallStatus(PhoneCallStatus.BILLED);

                        }
                        break;
                    }
                    case PAID: {
                        if (phoneCall.getPhoneCallStatus() == PhoneCallStatus.BILLED) {
                            phoneCall.setPhoneCallStatus(PhoneCallStatus.PAID);

                        }
                        break;

                    }
                }
            }
        }
    }


    /*@Override
    public List<PhoneCall> getCallsByDateRange(Long CustomerId, int startYear, int startMonth, int endYear, int endMonth, PhoneCallType type, int start, int maxResults) throws InstanceNotFoundException {
        List<PhoneCall> customerCalls = getCallsByUser(CustomerId);
        List<PhoneCall> validPhoneCalls = new java.util.ArrayList<>(List.of());
        int i = 0, callsInRange = 0, validCalls = 0;
        LocalDateTime start_date = LocalDateTime.of(startYear, startMonth, 1, 0, 0, 0, 0);
        LocalDateTime end_date = LocalDateTime.of(endYear, endMonth, 1, 0, 0, 0, 0);

        while (validCalls < maxResults && i < customerCalls.size()) {
            PhoneCall phoneCall = customerCalls.get(i);
            if ((start_date.isBefore(phoneCall.getStartDate()) || start_date.isEqual(phoneCall.getStartDate()))
                    && (end_date.isAfter(phoneCall.getStartDate()) || end_date.isEqual(phoneCall.getStartDate()))
                    && (type == null?true:type == phoneCall.getPhoneCallType())) {
                if (callsInRange >= start) {
                    validPhoneCalls.add(phoneCall);
                    validCalls++;
                }
                callsInRange++;
            }
            i++;
        }
        return validPhoneCalls;
    }*/

    @Override
    public List<PhoneCall> getCallsByDateRange(Long CustomerId, LocalDateTime start_date, LocalDateTime end_date, PhoneCallType type, int start, int maxResults) throws InstanceNotFoundException {
        List<PhoneCall> customerCalls = getCallsByUser(CustomerId);
        List<PhoneCall> validPhoneCalls = new java.util.ArrayList<>(List.of());
        int i = 0, callsInRange = 0, validCalls = 0;
        //LocalDateTime start_date = LocalDateTime.of(startYear, startMonth, 1, 0, 0, 0, 0);
        //LocalDateTime end_date = LocalDateTime.of(endYear, endMonth, 1, 0, 0, 0, 0);

        while (validCalls < maxResults && i < customerCalls.size()) {
            PhoneCall phoneCall = customerCalls.get(i);
            if ((start_date.isBefore(phoneCall.getStartDate()) || start_date.isEqual(phoneCall.getStartDate()))
                    && (end_date.isAfter(phoneCall.getStartDate()) || end_date.isEqual(phoneCall.getStartDate()))
                    && (type == null ? true : type == phoneCall.getPhoneCallType())) {
                if (callsInRange >= start) {
                    validPhoneCalls.add(phoneCall);
                    validCalls++;
                }
                callsInRange++;
            }
            i++;
        }
        return validPhoneCalls;
    }

    //TODO: crear un metodo que sea validar os Clientes e as Chamadas

    public Customer addCustomer(Customer c) throws InputValidationException {
        validateCustomer(c);
        c.setCustomerId(getNextClientId());
        c.setCreationDate(LocalDateTime.now());
        clientsMap.put(c.getCustomerId(), new Customer(c));
        return c;
    }

    public void updateCustomer(Customer c) throws InstanceNotFoundException, InputValidationException {
        validateCustomer(c);
        Customer c2 = clientsMap.get(c.getCustomerId());
        if (c2 == null) {
            throw new InstanceNotFoundException(c.getCustomerId(),
                    Customer.class.getName());
        }
        c2.setName(c.getName());
        c2.setAddress(c.getAddress());
        c2.setDni(c.getDni());
        clientsMap.replace(c.getCustomerId(), c2);
    }

    public void deleteCustomer(Long customerId) throws InstanceNotFoundException, CustomerHasCallsException {
        List<PhoneCall> customerPhoneCalls = phoneCallsByUserMap.get(customerId);
        if (customerPhoneCalls != null && !customerPhoneCalls.isEmpty()) {
            throw new CustomerHasCallsException(customerId);
        }
        Customer c = clientsMap.remove(customerId);
        if (c == null) {
            throw new InstanceNotFoundException(customerId, Customer.class.getName());
        }
        phoneCallsByUserMap.remove(customerId);
    }


    public PhoneCall addPhoneCall(PhoneCall p) throws InputValidationException, InstanceNotFoundException {
        validateCustomerExists(p.getCustomerId());
        validatePhoneCall(p);
        p.setPhoneCallId(getNextPhoneCallId());
        PhoneCall phoneCallInstance = new PhoneCall(p);
        phoneCallsMap.put(p.getPhoneCallId(), phoneCallInstance);
        phoneCallsByUserMap.computeIfAbsent(p.getCustomerId(), k -> new ArrayList<>());
        phoneCallsByUserMap.get(p.getCustomerId()).add(phoneCallInstance);
        return p;
    }

    @Override
    public Customer getCustomerByDNI(String dni) throws InstanceNotFoundException {

        for (Customer c : clientsMap.values()) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        throw new InstanceNotFoundException(dni, Customer.class.getName());

    }

    @Override
    public Customer getCustomerById(Long customerId) throws InstanceNotFoundException {
        Customer c = clientsMap.get(customerId);

        if (c == null) {
            throw new InstanceNotFoundException(customerId, Customer.class.getName());
        }
        return new Customer(c);
    }

    @Override
    public List<Customer> searchCustomersByName(String text, int start, int maxResults) throws InputValidationException {
        // Validación de los parámetros de entrada
        if (text == null || text.isEmpty()) {
            throw new InputValidationException("El texto de búsqueda no puede ser nulo o vacío.");
        }

        if (start < 0) {
            throw new InputValidationException("El valor de inicio no puede ser negativo.");
        }

        if (maxResults <= 0) {
            throw new InputValidationException("El número máximo de resultados debe ser mayor a 0.");
        }

        List<Customer> filteredCustomers = new ArrayList<>();

        // Filtrar clientes cuyo nombre contenga el texto de búsqueda (ignorando mayúsculas/minúsculas)
        for (Customer c : clientsMap.values()) {
            if (c.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredCustomers.add(c);
            }
        }

        // Si el índice de inicio es mayor que el tamaño de la lista filtrada, devolver una lista vacía
        if (start >= filteredCustomers.size()) {
            return new ArrayList<>();
        }

        // Cálculo de la sublista en base a paginación
        int end = Math.min(start + maxResults, filteredCustomers.size());
        return filteredCustomers.subList(start, end);
    }


    public List<PhoneCall> getCallsByUserDEBUG(Long CustomerId) {
        if (phoneCallsByUserMap.containsKey(CustomerId)) {
            return this.phoneCallsByUserMap.get(CustomerId);
        } else {
            return List.of();
        }
    }

    public List<PhoneCall> getCallsByCustomerAndMonthDEBUG(Long CustomerId, int year, int month) {
        List<PhoneCall> customerCalls = getCallsByUserDEBUG(CustomerId);

        List<PhoneCall> validPhoneCalls = new java.util.ArrayList<>(List.of());
        for (PhoneCall phoneCall : customerCalls) {
            if (phoneCall.getStartDate().getMonth().getValue() == month && phoneCall.getStartDate().getYear() == year) {
                validPhoneCalls.add(phoneCall);
            }
        }
        return validPhoneCalls;
    }

    public void cleanDatabase() {
        this.clientsMap.clear();
        this.phoneCallsMap.clear();
        this.phoneCallsByUserMap.clear();
        this.lastClientId = 0;
        this.lastPhoneCallId = 0;
    }

}
