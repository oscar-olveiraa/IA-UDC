package es.udc.rs.telco.model.telcoservice.exceptions;

public class CustomerHasCallsException extends Exception {
    public CustomerHasCallsException(Long id){
        super("Customer " + id + " has calls associated, remove not possible.");
    }
}
