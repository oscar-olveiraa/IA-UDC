package es.udc.rs.telco.model.telcoservice.exceptions;

public class DifferentStateCalls extends Exception {
    public DifferentStateCalls() {
        super("At least one call is in a different state compared to the given one");
    }
}
