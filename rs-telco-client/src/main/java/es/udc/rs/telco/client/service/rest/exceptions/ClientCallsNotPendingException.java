package es.udc.rs.telco.client.service.rest.exceptions;

public class ClientCallsNotPendingException extends Exception {
    public ClientCallsNotPendingException(String message){
        super(message);
    }
}
