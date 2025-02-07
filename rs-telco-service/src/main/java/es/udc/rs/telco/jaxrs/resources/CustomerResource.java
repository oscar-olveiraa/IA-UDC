package es.udc.rs.telco.jaxrs.resources;

import java.net.URI;
import java.util.List;

import es.udc.rs.telco.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.SearchedCustomerDtoJaxb;
import es.udc.rs.telco.jaxrs.util.CustomerToCustomerDtoJaxbConversor;
import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.telcoservice.exceptions.*;
import es.udc.rs.telco.model.telcoservice.TelcoService;
import es.udc.rs.telco.model.telcoservice.TelcoServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


@Path("customers")
@OpenAPIDefinition(
        info = @Info(
                title = "Aplicacion rs-telco",
                version = "3.0.1",
                description = "Documentacion acerca del servicio de telecomunicaciones para la gestión de clientes y sus llamadas"),
        servers = {
                @Server(
                        url = "http://localhost:7070/rs-telco-service")
        })

public class CustomerResource {

    private TelcoService telcoService = TelcoServiceFactory.getService();


    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Peticion para añadir de un cliente en la aplicacion", description = "Los parámetros del cliente se indican en cuerpo de la peticion",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
                    @ApiResponse(responseCode = "401", description = "Algún parámetro es incorrecto",
                            content = @Content(schema = @Schema(implementation = InputValidationException.class)))})
    public Response addCustomer(@RequestBody(description = "Objeto para crear cliente", required = true,
            content = @Content(schema = @Schema(implementation = CustomerDtoJaxb.class))) CustomerDtoJaxb customerDto, @Context final UriInfo ui) throws InputValidationException {

        Customer cust = CustomerToCustomerDtoJaxbConversor.toCustomer(customerDto);

        cust = telcoService.addCustomer(cust);
        final CustomerDtoJaxb resultCustomerDto = CustomerToCustomerDtoJaxbConversor.toCustomerDtoJaxb(cust);

        final String requestUri = ui.getRequestUri().toString();
        return Response.created(URI.create(requestUri + (requestUri.endsWith("/") ? "" : "/") + cust.getCustomerId()))
                .entity(resultCustomerDto).build();
    }


    @GET
    @Path("/{id: \\d+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Buscar un cliente por su ID", description = "Muestra informacion de un cliente buscando por su ID",
               responses = {
                       @ApiResponse(responseCode = "200", description="Cliente encontrado con éxito"),
                       @ApiResponse(responseCode = "404", description = "El cliente no existe",
                               content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))})
    public CustomerDtoJaxb findCustomerById(@Parameter(description = "ID del cliente que queremos buscar", required = true) @PathParam("id") String id) throws InstanceNotFoundException {
        Long custId;
        try {
            custId = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            throw new InstanceNotFoundException(id, "2"); //corregir parametros, son de xoguete
        }

        return CustomerToCustomerDtoJaxbConversor.toCustomerDtoJaxb(telcoService.getCustomerById(custId));

        /*
        Long custId;
        try {
            custId = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            throw new InstanceNotFoundException(id, "1"); //corregir parametros, son de xoguete
        }
        //Customer cust = TelcoServiceFactory.getService().getCustomerById(custId);
        CustomerDtoJaxb customerDto = new CustomerDtoJaxb(
                1L,
                "John Doe",
                "12345678X",
                "123 Main St",
                "+34 600 123 456"
        );
        return customerDto;*/

    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id:}")
    @Operation(summary = "Actualizar los datos de un cliente",
            description = "Los datos del cliente que queremos actualizar van el cuerpo de la petición",
            responses = {
                    @ApiResponse(responseCode  = "200", description="Cliente actualizado con éxito"),
                    @ApiResponse(responseCode = "400", description = "Alguno de los parametros que están en el cuerpo son incorrectos",
                            content = @Content(schema = @Schema(implementation = InputValidationException.class))),
                    @ApiResponse(responseCode = "404", description = "El cliente no existe",
                            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))})
       public void updateCustomer(CustomerDtoJaxb customerDto, @PathParam("id") final String id) throws InstanceNotFoundException, InputValidationException {

        Long custId;
        try {
            custId = Long.valueOf(id);
        } catch (final NumberFormatException ex) {
            throw new InputValidationException("Invalid Request: " + "unable to parse customer id '" + id + "'");
        }

        if (!custId.equals(customerDto.getCustomerId())) {
            throw new InputValidationException(
                    "Invalid Request: invalid customer Id '" + customerDto.getCustomerId() + "' for customer '" + custId + "'");
        }

        final Customer customer = CustomerToCustomerDtoJaxbConversor.toCustomer(customerDto);
        telcoService.updateCustomer(customer);
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{dni}")
    @Operation(summary = "Buscar un cliente por su DNI", description = "Muestra el cliente con el DNI que se especifica en la URL",
            responses = {
                @ApiResponse(responseCode = "200", description="Cliente encontrado con éxito"),
                @ApiResponse(responseCode = "404", description = "El cliente con ese DNI no existe",
                    content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))})
    public CustomerDtoJaxb getCustomerByDNI(@Parameter(description = "DNI del cliente que queremos buscar", required = true) @PathParam("dni") String dni) throws InstanceNotFoundException {

        final Customer customer = telcoService.getCustomerByDNI(dni);
        return CustomerToCustomerDtoJaxbConversor.toCustomerDtoJaxb(customer);

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Busqueda de clientes por su nombre", description = "Devuelve lista de clientes que coincidan con el patrón de caracteres especificado",
            responses = {
                    @ApiResponse(responseCode = "200", description="Devuelve los clientes que coinciden con nombre especificado"),
                    @ApiResponse(responseCode = "404", description = "Alguno los parámetros establecidos en la petición no son correctos",
                            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))})
    public List<SearchedCustomerDtoJaxb> searchCustomersByName(@Parameter(description = "nombre del cliente a buscar", required = true) @QueryParam("keywords") final String keywords,
                                                               @Parameter(description = "Indice sobre el que vamos hacer la búsqueda", required = true) @QueryParam("startIndex") final String SstartIndex,
                                                               @Parameter(description = "Numero de clientes que se mostrarán", required = true) @QueryParam("count") final String Scount) throws InputValidationException {
        int startIndex,count;
        try {
            startIndex = Integer.valueOf(SstartIndex);
            count = Integer.valueOf(Scount);
            final List<Customer> customers = telcoService.searchCustomersByName(keywords, startIndex, count);
            return CustomerToCustomerDtoJaxbConversor.toSearchedCustomerDtoJaxb(customers);
        } catch (NumberFormatException ex) {
            throw new InputValidationException(ex.getMessage());
        }
    }


    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id: \\d+}")
    @Operation(summary = "Eliminar un cliente que está registrado",
            description = "Se especifica en la url el ID del cliente, pero no puede tener ningunha llamada realizada",
            responses = {
                    @ApiResponse(responseCode  = "200", description="Cliente eliminado con éxito"),
                    @ApiResponse(responseCode = "404", description = "No existe ese usuario",
                            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class))),
                    @ApiResponse(responseCode = "409", description = "El cliente no se puede eliminar porque tiene llamadas en su historial",
                            content = @Content(schema = @Schema(implementation = CustomerHasCallsException.class)))})
    public void deleteClient(@PathParam("id") final String id)
            throws InstanceNotFoundException, CustomerHasCallsException {

        Long customerId;
        customerId = Long.valueOf(id);
        telcoService.deleteCustomer(customerId);
    }
}


