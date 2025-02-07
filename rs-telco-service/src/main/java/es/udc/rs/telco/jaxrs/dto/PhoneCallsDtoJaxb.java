package es.udc.rs.telco.jaxrs.dto;

import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.time.LocalDateTime;

@XmlRootElement(name = "phoneCall")
@XmlType(name = "phoneCallJaxbType", propOrder = {"callId", "customerId", "startTime", "duration",
        "destinationNumber", "callType", "callStatus"})
public class PhoneCallsDtoJaxb {

    @XmlAttribute(name = "callId", required = true)
    protected Long callId;

    @XmlElement(required = true)
    protected Long customerId;

    @XmlElement(required = true)
    private String startTime;

    @XmlElement(required = true)
    private Long duration;

    @XmlElement(required = true)
    private String destinationNumber;

    @XmlElement(required = true)
    protected String callType;

    @XmlElement(required = true)
    protected String callStatus;

    public PhoneCallsDtoJaxb() {
    }

    public PhoneCallsDtoJaxb(Long customerId, String startDate, Long duration, String destinationNumber, String phoneCallType, String phoneCallStatus) {
        this.customerId = customerId;
        this.startTime = startDate;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
        this.callType = phoneCallType;
        this.callStatus = phoneCallStatus;
    }

    public PhoneCallsDtoJaxb(Long callId, Long customerId, String startTime,
                             Long duration, String destinationNumber,
                             String callType, String callStatus) {
        this.callId = callId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
        this.callType = callType;
        this.callStatus = callStatus;
    }

    // Getters y setters (sin cambios)
    @Schema(description = "ID de la llamada", allowableValues =  {}, required=true)
    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    @Schema(description = "ID del cliente", allowableValues =  {}, required=true)
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Schema(description = "Fecha y hora de inicio de la llamada", allowableValues =  {}, required=true)
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Schema(description = "Duración de la llamada", allowableValues =  {}, required=true)
    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Schema(description = "Número de teléfono de destino de la llamada", allowableValues =  {}, required=true)
    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    @Schema(description = "Tipo de llamada", allowableValues =  {}, required=true)
    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    @Schema(description = "Estado de la llamada", allowableValues =  {}, required=true)
    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    @Override
    public String toString() {
        return "PhoneCallsDtoJaxb{" +
                "callId=" + callId +
                ", customerId=" + customerId +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                ", destinationNumber='" + destinationNumber + '\'' +
                ", callType='" + callType + '\'' +
                ", callStatus='" + callStatus + '\'' +
                '}';
    }
}

