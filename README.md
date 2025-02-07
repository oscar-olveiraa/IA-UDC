# Esta repo contén tamén o traballo tutelado feito:

**Carpeta [ia_tutelado](/ia_tutelado/)**

# Nota final e correccións:

**NOTA** -> 7.7 (perdemos 1.5 puntos por non facer Hipermedia)

**Erros:**

- En cliente, temos faltanos un DTO simplificado das chamadas, xa que o que usamos devolvenos todo en vez dos campos que necesitamos

- En servicio, o recurso para obter cliente por DNI, o DNI ten que ir nunha QueryParam en vez de de un PathParam

- En servicio, o recurso para buscar chamadas de un cliente por mes, o id do cliente ten que ir nunha QueryParam en vez de un PathParam

- En BPEL, non cacheamos a excepcion, o resto si que o facemos ben


# Running the project example
---------------------------------------------------------------------

## Running the telco service with Maven/Jetty.

    cd rs-telco/rs-telco-service
    mvn jetty:run


## Running the telco client application

- Configure `rs-telco/rs-telco-client/src/main/resources/ConfigurationParameters.properties`
  for specifying the client project service implementation (XML or JSON) and the port number 
  of the web server in the endpoint address (7070 for Jetty)
  
- Change to `rs-telco-client` folder

    cd rs-telco/rs-telco-client


- AddCustomer

    mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-addCustomer 'New Customer'"

- FindCustomer

    mvn exec:java -Dexec.mainClass="es.udc.rs.telco.client.ui.TelcoServiceClient" -Dexec.args="-findCustomer 1"



