package es.udc.rs.telco.model.telcoservice.exceptions;

public class MonthNotExpiredException extends Exception {
    public MonthNotExpiredException() {
        super("Month haven't expired yet");
    }
}
