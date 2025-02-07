package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "instanceNotFoundException")
@XmlType(name="instanceNotFoundExceptionJaxbType", propOrder = {"instanceId", "instanceType"})
public class InstanceNotFoundExceptionDtoJaxb {

    @XmlElement(required = true)
    private String instanceId;
    @XmlElement(required = true)
    private String instanceType;

    public InstanceNotFoundExceptionDtoJaxb(){}

    public InstanceNotFoundExceptionDtoJaxb(Object instanceId, String instanceType) {
        this.instanceId = instanceId.toString();
        if (instanceType != null) {
            this.instanceType = instanceType.substring(instanceType.lastIndexOf('.') + 1);
        }
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    @Override
    public String toString() {
        return "InstanceNotFoundExceptionDtoJaxb{" +
                "instanceId='" + instanceId + '\'' +
                ", instanceType='" + instanceType + '\'' +
                '}';
    }

}