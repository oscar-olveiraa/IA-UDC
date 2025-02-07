package es.udc.rs.telco.client.service;

import java.time.LocalDateTime;

public class ClientPhoneCallDto {
    private Long callId = null;
    private Long customerId = null;
    private LocalDateTime startTime = null;
    private Long duration = null;
    private String destinationNumber = null;
    private String callType = null;
    private String callStatus = null;

    public ClientPhoneCallDto() {}

    public ClientPhoneCallDto(Long callId,Long customerId, LocalDateTime startTime, Long duration, String destinationNumber, String callType, String callStatus) {
        this.callId = callId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
        this.callType = callType;
        this.callStatus = callStatus;
    }

    public ClientPhoneCallDto(LocalDateTime startTime, Long duration, String destinationNumber) {
        this.startTime = startTime;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
    }

    public ClientPhoneCallDto(Long customerId, LocalDateTime startDate, Long duration, String destinationNumber, String phoneCallType) {
        this.customerId = customerId;
        this.startTime = startDate;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
        this.callType = phoneCallType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }
}
