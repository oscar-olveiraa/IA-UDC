package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement
public class SearchedPhoneCallsDtoJaxb extends PhoneCallsDtoJaxb{
    @XmlTransient
    protected Long callId;

    @XmlTransient
    protected Long customerId;

    @XmlTransient
    protected String callType;

    @XmlTransient
    protected String callStatus;

    public SearchedPhoneCallsDtoJaxb(){}

    public SearchedPhoneCallsDtoJaxb(Long customerId, String startDate, Long duration, String destinationNumber, String phoneCallType, String phoneCallStatus){
        super(customerId, startDate, duration, destinationNumber, phoneCallType, phoneCallStatus);
    }
    public SearchedPhoneCallsDtoJaxb(Long callId, Long customerId, String startTime,
                                     Long duration, String destinationNumber,
                                     String callType, String callStatus){
        super(callId, customerId,startTime, duration, destinationNumber, callType, callStatus);
    }

    @Override
    public String toString() {
        return "PhoneCallsDtoJaxb{" +
                " startTime='" + super.getStartTime() + '\'' +
                ", duration=" + super.getDuration() +
                ", destinationNumber='" + super.getDestinationNumber() + '\'' +
                '}';
    }
}
