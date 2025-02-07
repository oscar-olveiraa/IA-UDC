package es.udc.rs.telco.client.service.rest;

import com.fasterxml.jackson.core.util.JacksonFeature;
import es.udc.rs.telco.client.service.ClientCustomerDto;
import es.udc.rs.telco.client.service.ClientPhoneCallDto;
import es.udc.rs.telco.client.service.rest.dto.*;
import es.udc.rs.telco.client.service.rest.exceptions.*;
import es.udc.rs.telco.client.service.rest.json.JaxbJsonContextResolver;
import es.udc.rs.telco.client.service.util.ClientCustomerDtoToCustomerDtoJaxbConversor;
import es.udc.rs.telco.client.service.util.JaxbExceptionConversor;
import es.udc.rs.telco.client.service.util.PhoneCallDtoToPhoneCallDtoJaxbConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import es.udc.rs.telco.client.service.ClientTelcoService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

public abstract class RestClientTelcoService implements ClientTelcoService {

    private static jakarta.ws.rs.client.Client client = null;

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientTelcoService.endpointAddress";
    private WebTarget endPointWebTarget = null;
    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    /*
     * Client instances are expensive resources. It is recommended a configured
     * instance is reused for the creation of Web resources. The creation of Web
     * resources, the building of requests and receiving of responses are
     * guaranteed to be thread safe. Thus a Client instance and WebTarget
     * instances may be shared between multiple threads.
     */
    private static Client getClient() {
        if (client == null) {
            client = ClientBuilder.newClient();
            client.register(JacksonFeature.class);
            client.register(JaxbJsonContextResolver.class);
        }
        return client;
    }

    private WebTarget getEndpointWebTarget() {
        if (endPointWebTarget == null) {
            endPointWebTarget = getClient()
                    .target(ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER));
        }
        return endPointWebTarget;
    }

    protected abstract MediaType getMediaType();

    @Override
    public Long addCustomer(ClientCustomerDto cust) throws InputValidationException {

        try (Client client = getClient()) {
            WebTarget wt = client.target(endpointAddress).path("customers");
            try (Response response = wt.request()
                    .accept(this.getMediaType())
                    .post(Entity.entity(
                            ClientCustomerDtoToCustomerDtoJaxbConversor.toJaxbCustomer(cust),
                            this.getMediaType()))) {
                validateResponse(Response.Status.CREATED, response);
                CustomerJaxbType resultCustomer = response.readEntity(CustomerJaxbType.class);
                return resultCustomer.getCustomerId();
            } catch (InputValidationException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public Long addPhoneCall(ClientPhoneCallDto call) throws InputValidationException, InstanceNotFoundException {
        try (Client client = getClient()) {
            WebTarget wt = client.target(endpointAddress).path("phoneCall");

            try (Response response = wt.request()
                    .accept(this.getMediaType())
                    .post(Entity.entity(
                            PhoneCallDtoToPhoneCallDtoJaxbConversor.toJaxbTelco(call), this.getMediaType()))) {
                validateResponse(Response.Status.CREATED, response);
                PhoneCallJaxbType resultPhone = response.readEntity(PhoneCallJaxbType.class);
                return resultPhone.getCallId();
            } catch (InputValidationException | InstanceNotFoundException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteClient(Long customerId) throws InstanceNotFoundException, ClientCustomerHasCallsException {

        try (Client client = getClient()) {
            WebTarget wt = client.target(endpointAddress).path("customers/{id}").resolveTemplate("id", customerId);

            try (Response response = wt.request().delete()) {
                validateResponse(Response.Status.NO_CONTENT, response); // HTTP 204: Sin contenido, éxito al eliminar.
            } catch (InstanceNotFoundException | ClientCustomerHasCallsException ex) {
                throw ex; // Si el cliente no existe, lanzar la excepción.
            } catch (Exception ex) {
                throw new RuntimeException(ex); // Manejar cualquier otra excepción inesperada.
            }
        }
    }


    @Override
    public void changeCallStatus(Long customerId, int month, int year, String newStatus) throws InputValidationException, InstanceNotFoundException, ClientMonthNotExpired, ClientDifferentStateCallsException {
        try (Client client = getClient()) {
            WebTarget wt = client.target(endpointAddress).path("phoneCall/updateStatusCall");
            try (Response response = wt.request()
                    .accept(this.getMediaType())
                    .post(Entity.form(new Form().param("customerId", customerId.toString())
                            .param("month", Integer.toString(month))
                            .param("year", Integer.toString(year))
                            .param("newStatus", newStatus)))) {
                validateResponse(Response.Status.NO_CONTENT, response);
            } catch (InputValidationException | InstanceNotFoundException| ClientMonthNotExpired| ClientDifferentStateCallsException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public List<ClientPhoneCallDto> searchCallsByDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate, int start, int maxResults, String type) throws InputValidationException, InstanceNotFoundException {
        try (Client client = getClient()) {
            WebTarget wt = client.target(endpointAddress);
            if (type == null) {
                wt = wt.path("phoneCall/{id}").resolveTemplate("id", customerId)
                        .queryParam("startDate", PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorSearchLocalDate(startDate))
                        .queryParam("endDate", PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorSearchLocalDate(endDate))
                        .queryParam("start", start)
                        .queryParam("maxResults", maxResults);
            } else {
                wt = wt.path("phoneCall/{id}").resolveTemplate("id", customerId)
                        .queryParam("startDate", PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorSearchLocalDate(startDate))
                        .queryParam("endDate", PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorSearchLocalDate(endDate))
                        .queryParam("type", type)
                        .queryParam("start", start)
                        .queryParam("maxResults", maxResults);
            }
            try (Response response = wt.request().accept(this.getMediaType()).get()) {
                validateResponse(Response.Status.OK, response);
                List<SearchedPhoneCallsDtoJaxb> phoneCalls =
                        response.readEntity(new GenericType<List<SearchedPhoneCallsDtoJaxb>>() {
                        });
                return PhoneCallDtoToPhoneCallDtoJaxbConversor.toPhoneCallDtosFromSearched(phoneCalls);
            } catch (InputValidationException | InstanceNotFoundException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    /*private void validateResponse(Response.Status expected, Response received) throws InputValidationException {
        if (received.getStatus() == expected.getStatusCode()) {

            if ((received.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) ||
                    (received.getStatus() == Response.Status.CREATED.getStatusCode())) {

            } else if (this.getMediaType().equals(received.getMediaType())) {

            } else {

                throw new RuntimeException("HTTP Error: Incompatible MediaType");
            }
        }
    }*/

    private void validateResponse(Response.Status expected, Response received) throws InputValidationException, InstanceNotFoundException, ClientCustomerHasCallsException, ClientCallsNotPendingException, ClientDifferentStateCallsException, ClientMonthNotExpired {

        if (received.getStatus() == expected.getStatusCode()) {
            if ((received.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) ||
                    (received.getStatus() == Response.Status.CREATED.getStatusCode())) {
            } else if (this.getMediaType().equals(received.getMediaType())) {
                return;
            } else {
                throw new RuntimeException("HTTP Error: Incompatible MediaType");
            }
        } else {
            switch (Response.Status.fromStatusCode(received.getStatus())) {
                case BAD_REQUEST:
                    InputValidationExceptionJaxbType exInputVal = received.readEntity(InputValidationExceptionJaxbType.class);
                    throw JaxbExceptionConversor.toInputValidationException(exInputVal);
                    //throw new InputValidationException(exInputVal.getMessage());
                case NOT_FOUND:
                    InstanceNotFoundExceptionJaxbType exNotFound = received.readEntity(InstanceNotFoundExceptionJaxbType.class);
                    throw JaxbExceptionConversor.toInstanceNotFoundException(exNotFound);
                    //throw new InstanceNotFoundException(exNotFound.getInstanceId(), exNotFound.getInstanceType());
                case CONFLICT:
                    ApplicationExceptionJaxbType appEx = received.readEntity(ApplicationExceptionJaxbType.class);
                    switch (appEx.getErrorType()) {
                        case "CustomerHasCalls":
                            throw new ClientCustomerHasCallsException(appEx.getMessage());
                        case "CallsNotPending":
                            throw new ClientCallsNotPendingException(appEx.getMessage());
                        case "DifferentStateCalls":
                            throw new ClientDifferentStateCallsException(appEx.getMessage());
                        case "MonthNotExpired":
                            throw new ClientMonthNotExpired(appEx.getMessage());
                    }
                default:
                    throw new RuntimeException("Unknown error. HTTP Status code = " + received.getStatus());
            }
        }

    }


}
