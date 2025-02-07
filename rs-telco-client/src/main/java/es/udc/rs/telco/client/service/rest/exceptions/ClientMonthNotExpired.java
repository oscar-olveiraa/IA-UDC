package es.udc.rs.telco.client.service.rest.exceptions;

public class ClientMonthNotExpired extends Exception {
    public ClientMonthNotExpired (String message){
        super(message);
    }
}
