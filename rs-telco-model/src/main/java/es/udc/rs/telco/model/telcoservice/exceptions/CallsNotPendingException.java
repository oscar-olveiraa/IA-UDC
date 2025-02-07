package es.udc.rs.telco.model.telcoservice.exceptions;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class CallsNotPendingException extends Exception {
    public CallsNotPendingException(int year, Month month) {
        super("Not all the customer's calls made during " + month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)+ "of "+ year +"  are pending");
    }
}
