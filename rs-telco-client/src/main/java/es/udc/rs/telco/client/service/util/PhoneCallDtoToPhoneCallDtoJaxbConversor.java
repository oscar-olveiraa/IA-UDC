package es.udc.rs.telco.client.service.util;

import es.udc.rs.telco.client.service.ClientCustomerDto;
import es.udc.rs.telco.client.service.ClientPhoneCallDto;
import es.udc.rs.telco.client.service.rest.dto.ObjectFactory;
import es.udc.rs.telco.client.service.rest.dto.PhoneCallJaxbType;
import es.udc.rs.telco.client.service.rest.dto.SearchedPhoneCallsDtoJaxb;
import jakarta.xml.bind.JAXBElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PhoneCallDtoToPhoneCallDtoJaxbConversor{

    public static JAXBElement<PhoneCallJaxbType> toJaxbTelco(ClientPhoneCallDto phoneCallDto) {
        PhoneCallJaxbType phoneCallJaxb = new PhoneCallJaxbType();
        phoneCallJaxb.setCallId(phoneCallDto.getCustomerId() != null? phoneCallDto.getCustomerId(): -1);
        //phoneCallJaxb.setCallStatus(phoneCallDto.getCallStatus());
        phoneCallJaxb.setCustomerId(phoneCallDto.getCustomerId());
        phoneCallJaxb.setDuration(phoneCallDto.getDuration());
        phoneCallJaxb.setStartTime(conversorLocalDatetoString(phoneCallDto.getStartTime()));
        phoneCallJaxb.setDestinationNumber(phoneCallDto.getDestinationNumber());
        phoneCallJaxb.setCallType(phoneCallDto.getCallType());
        return new ObjectFactory().createPhoneCall(phoneCallJaxb);
    }

    public static ClientPhoneCallDto toPhoneCallDto(PhoneCallJaxbType phoneCall) {
        return new ClientPhoneCallDto(phoneCall.getCallId(), phoneCall.getCustomerId(), conversorLocalDatefromString(phoneCall.getStartTime()),phoneCall.getDuration(),phoneCall.getDestinationNumber(),phoneCall.getCallType(),phoneCall.getCallStatus());
    }

    public static List<ClientPhoneCallDto> toPhoneCallDtosFromPhoneCall(List<PhoneCallJaxbType> phoneCallListDto) {
        List<ClientPhoneCallDto> phoneCallDtos = new ArrayList<>(phoneCallListDto.size());
        for (PhoneCallJaxbType phoneCallDtoJaxb : phoneCallListDto) {
            phoneCallDtos.add(toPhoneCallDto(phoneCallDtoJaxb));
        }
        return phoneCallDtos;
    }

    public static List<ClientPhoneCallDto> toPhoneCallDtosFromSearched(List<SearchedPhoneCallsDtoJaxb> phoneCallListDto) {
        List<ClientPhoneCallDto> phoneCallDtos = new ArrayList<>(phoneCallListDto.size());
        for (SearchedPhoneCallsDtoJaxb phoneCallDtoJaxb : phoneCallListDto) {
            phoneCallDtos.add(toPhoneCallDto(phoneCallDtoJaxb));
        }
        return phoneCallDtos;
    }

    public static ClientPhoneCallDto toPhoneCallDto(SearchedPhoneCallsDtoJaxb searchedPhoneCall) {
        return new ClientPhoneCallDto(conversorLocalDatefromString(searchedPhoneCall.getStartTime()),searchedPhoneCall.getDuration(),searchedPhoneCall.getDestinationNumber());
    }

    public static String conversorLocalDatetoString(LocalDateTime startDate){

        String formattedDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.n]");
        formattedDate = startDate.format(formatter);

        return formattedDate;
    }

    public static String conversorSearchLocalDate(LocalDateTime startDate){

        String formattedDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formattedDate = startDate.format(formatter);

        return formattedDate;
    }

    public static LocalDateTime conversorLocalDatefromString(String startDate){

        LocalDateTime formattedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedDate = LocalDateTime.parse(startDate, formatter);
        } catch (DateTimeParseException e) {
            formattedDate = LocalDateTime.parse(startDate);
        }
        return formattedDate;
    }

}
