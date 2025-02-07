### **Añadir Clientes:**

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-addCustomer 'Cliente Primero' '12345678J' 'Elviña s/n' 981111111"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-addCustomer 'Cliente Segundo' '87654321H' 'María Pita s/n' 981222222"

### **Actualizar cliente [POSTMAN]**

**XML**

```XML
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<customer xmlns="http://ws.udc.es/telco/xml" customerId="2">
    <name>Cliente Segundo Modificado</name>
    <dni>87654321H</dni>
    <address>María Pita nº1</address>
    <phoneNumber>981222222</phoneNumber>
</customer>
```

**JSON**

```JSON
{
  "customerId": "2",
  "name": "Cliente Segundo Modificado",
  "dni": "87654321H",
  "address": "María Pita nº1",
  "phoneNumber": "601635600"
}
```


### **Buscar cliente por id [POSTMAN]**


### **Buscar cliente por DNI [POSTMAN]**


### **Buscar cliente por palabras clave [POSTMAN]**


### **Crear llamadas**

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-addPhoneCall 1 '2024-10-01 11:00:00' 100 981100001 LOCAL"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-addPhoneCall 1 '2024-10-02 12:00:00' 200 981100002 NATIONAL"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-addPhoneCall 1 '2024-10-03 13:00:00' 300 981100003 INTERNATIONAL"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-addPhoneCall 1 '2024-11-04 14:00:00' 400 981100004 LOCAL"

### **Obtener llamadas para facturar [POSTMAN]**


### **Cambiar estado**

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-changeCallStatus 1 11 2024 BILLED"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-changeCallStatus 1 10 2024 PAID"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-changeCallStatus 1 10 2024 BILLED"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-changeCallStatus 1 10 2024 PAID"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-changeCallStatus 1 10 2024 PAID"

### **Obtener llamadas para facturar [POSTMAN]**


### **Obtener llamadas entre dos fechas**

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-searchCallsByDateRange 1 '2024-10-01 00:00:00' '2024-11-30 23:59:59' 0 10"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-searchCallsByDateRange 1  '2024-10-01 00:00:00' '2024-11-30 23:59:59' 0 10 LOCAL"


### **Eliminar cliente**

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-deleteClient 1"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-deleteClient 2"

mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-deleteClient 4"



