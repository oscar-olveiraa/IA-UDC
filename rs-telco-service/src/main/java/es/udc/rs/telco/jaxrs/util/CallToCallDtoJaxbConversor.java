package es.udc.rs.telco.jaxrs.util;

import es.udc.rs.telco.jaxrs.dto.PhoneCallsDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.SearchedPhoneCallsDtoJaxb;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import es.udc.ws.util.exceptions.InputValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CallToCallDtoJaxbConversor {
    public static List<PhoneCallsDtoJaxb> phoneCallsDtoJaxbs(List<PhoneCall> call) {
        List<PhoneCallsDtoJaxb> callDtos = new ArrayList<>(call.size());
        for (PhoneCall calls : call) {
            callDtos.add(toPhoneCallsDtoJaxb(calls));
        }
        return callDtos;
    }

    public static PhoneCallsDtoJaxb toPhoneCallsDtoJaxb(PhoneCall call) {
        return new PhoneCallsDtoJaxb(call.getPhoneCallId(),call.getCustomerId(), call.getStartDate().toString(), call.getDuration(), call.getDestinationNumber(),
                call.getPhoneCallType().toString(), call.getPhoneCallStatus().toString());
    }

    public static List<SearchedPhoneCallsDtoJaxb> toSearchedPhoneCallsDtoJaxbs(List<PhoneCall> call) {
        List<SearchedPhoneCallsDtoJaxb> callDtos = new ArrayList<>(call.size());
        for (PhoneCall calls : call) {
            callDtos.add(toSearchedPhoneCallsDtoJaxb(calls));
        }
        return callDtos;
    }

    public static SearchedPhoneCallsDtoJaxb toSearchedPhoneCallsDtoJaxb(PhoneCall call) {
        return new SearchedPhoneCallsDtoJaxb(call.getPhoneCallId(),call.getCustomerId(), call.getStartDate().toString(), call.getDuration(), call.getDestinationNumber(),
                call.getPhoneCallType().toString(), call.getPhoneCallStatus().toString());
    }

    public static PhoneCall toPhoneCall(PhoneCallsDtoJaxb call) throws InputValidationException{
        if (call == null) {
            throw new IllegalArgumentException("phoneCallDto cannot be null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.n]");
        LocalDateTime startTime = LocalDateTime.parse(call.getStartTime(), formatter);

        PhoneCallType callType = CallTypeConvertFromStr(call.getCallType());
        if (callType == null) {
            throw new InputValidationException("Invalid phone call type: " + call.getCallType());
        }

        PhoneCallStatus callStatus = CallStatusFromStr(call.getCallStatus());
        if (callStatus == null) {
            throw new InputValidationException("Invalid phone call status: " + call.getCallStatus());
        }

        return new PhoneCall(call.getCustomerId(), startTime, call.getDuration(), call.getDestinationNumber(),
                callType, callStatus);
    }

    public static PhoneCallType CallTypeConvertFromStr(String ScallType) throws InputValidationException{
        return switch (ScallType.toUpperCase()) {
            case "LOCAL" -> PhoneCallType.LOCAL;
            case "NATIONAL" -> PhoneCallType.NATIONAL;
            case "INTERNATIONAL" -> PhoneCallType.INTERNATIONAL;
            default -> null;
        };
    }

    public static PhoneCallStatus CallStatusFromStr(String SCallStatus){
        String status;

        if (SCallStatus == null){
            status = "";
        }else{
            status = SCallStatus.toUpperCase();
        }

        return switch (status) {
            case "PENDING" -> PhoneCallStatus.PENDING;
            case "BILLED" -> PhoneCallStatus.BILLED;
            case "PAID" -> PhoneCallStatus.PAID;
            default -> PhoneCallStatus.PENDING;
        };
    }

}
