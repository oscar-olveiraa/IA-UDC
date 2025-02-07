package es.udc.rs.telco.jaxrs.xml;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDateTime;

public class Adapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public String marshal(LocalDateTime arg0) throws Exception {
        return arg0.toString();
    }

    @Override
    public LocalDateTime unmarshal(String arg) throws Exception {
        return LocalDateTime.parse(arg);
    }
}

