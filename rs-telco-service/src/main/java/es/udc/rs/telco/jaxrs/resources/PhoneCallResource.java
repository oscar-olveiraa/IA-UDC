package es.udc.rs.telco.jaxrs.resources;

import es.udc.rs.telco.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.PhoneCallsDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.SearchedPhoneCallsDtoJaxb;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.jaxrs.util.CallToCallDtoJaxbConversor;
import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import es.udc.rs.telco.model.telcoservice.TelcoService;
import es.udc.rs.telco.model.telcoservice.TelcoServiceFactory;
import es.udc.rs.telco.model.telcoservice.exceptions.CallsNotPendingException;
import es.udc.rs.telco.model.telcoservice.exceptions.DifferentStateCalls;
import es.udc.rs.telco.model.telcoservice.exceptions.MonthNotExpiredException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("phoneCall")
public class PhoneCallResource {

    private TelcoService telcoService = TelcoServiceFactory.getService();

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Peticion para añadir llamadas", description = "Los parámetros de la llamada deben indicarse en el cuerpo de la petición",
               responses = {
                       @ApiResponse(responseCode = "201", description = "Llamada creada exitosamente"),
                       @ApiResponse(responseCode = "400", description = "Los argumentos del cuerpo son incorrectos",
                               content = @Content(schema = @Schema(implementation = InputValidationException.class))),
                       @ApiResponse(responseCode = "404", description = "El ID del cliente no existe",
                               content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))})
    public Response addPhoneCall(@RequestBody(description = "Objeto para crear llamadas", required = true,
            content = @Content(schema = @Schema(implementation = PhoneCallsDtoJaxb.class))) PhoneCallsDtoJaxb newCall, @Context UriInfo ui) throws InstanceNotFoundException, InputValidationException {

        PhoneCall call = CallToCallDtoJaxbConversor.toPhoneCall(newCall);

        /*PhoneCall call = new PhoneCall(
                        1L, LocalDateTime.now(), 23L, "123456789", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);*/

        call = telcoService.addPhoneCall(call);
        final PhoneCallsDtoJaxb resultCallDto = CallToCallDtoJaxbConversor.toPhoneCallsDtoJaxb(call);

        String requestUri = ui.getRequestUri().toString();
        return Response.created(URI.create(requestUri + (requestUri.endsWith("/") ? "" : "/") + call.getPhoneCallId())).entity(resultCallDto).build();
    }


    @POST
    @Path("/updateStatusCall")
    @Operation(summary = "Cambiar el estado de las llamadas",
            description = "Las llamadas se pueden actualizar de PENDING a BILLED, o de BILLED a PAID indicando los datos necesarios en el formulario de la peticion",
            responses ={
                    @ApiResponse(responseCode  = "204", description="Llamadas actualizadas con éxito"),
                    @ApiResponse(responseCode = "400", description="Los argumentos del formulario son incorrectos",
                            content = @Content(schema = @Schema(implementation = InputValidationException.class))),
                    @ApiResponse(responseCode = "404", description="El ID de cliente no existe",
                            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class))),
                    @ApiResponse(responseCode = "409", description="Erro al intentar el estado de las llamadas de un mes que aún no acabó",
                            content = @Content(schema = @Schema(implementation = MonthNotExpiredException.class))),
                    @ApiResponse(responseCode = "409", description="Error al intentar cambiar el estado de una llamada a un estado incorrecto",
                            content = @Content(schema = @Schema(implementation = DifferentStateCalls.class)))})
    public void changeStatus(@Parameter(description = "ID del cliente al que queremos actualizar sus llamadas", required = true) @FormParam("customerId") String ScustomerId,
                             @Parameter(description = "Mes en que se realizaron llamadas que queremos cambiar", required = true) @FormParam("month") String Smonth,
                             @Parameter(description = "Año en que se realizaron llamadas que queremos cambiar", required = true) @FormParam("year") String Syear,
                             @Parameter(description = "Estado nuevo de las llamadas de ese mes-año", required = true) @FormParam("newStatus") String SnewStatus)
            throws InstanceNotFoundException, DifferentStateCalls, MonthNotExpiredException, InputValidationException{

        Long customerId;
        Integer month, year;

        try {
            customerId = Long.valueOf(ScustomerId);
            month = Integer.valueOf(Smonth);
            year = Integer.valueOf(Syear);
        }catch(NumberFormatException e){
            throw new InputValidationException(e.getMessage());
        }

        telcoService.updateCallStatus(customerId, year, month, CallToCallDtoJaxbConversor.CallStatusFromStr(SnewStatus));

        // TODO: Debería devolver un ok?
        //return Response.ok().build();
    }

    @GET
    @Path("/{customerId: \\d+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Buscar llamadas en un rango de fechas",
            description = "Buscar llamadas en un intervalo de tiempo. De forma opcional, se puede especificar el tipo de llamadas que queremos obtener en ese rango (LOCAL, NACIONAL o INTERNACIONAL)",
            responses = {
                    @ApiResponse(responseCode  = "200", description="Devuelve las llamadas de ese intervalo"),
                    @ApiResponse(responseCode = "400", description="Los parámetros de la petición son inválidos o incorrectos",
                            content = @Content(schema = @Schema(implementation = InputValidationException.class))),
                    @ApiResponse(responseCode = "404", description="El ID del cliente no existe",
                            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))})
    public List<SearchedPhoneCallsDtoJaxb> searchCallsByDateRange(@Parameter(description = "ID del cliente que realizó las llamadas", required = true) @PathParam("customerId") String ScustomerId,
                                                                  @Parameter(description = "Fecha de inicio del intervalo de búsqueda", required = true) @QueryParam("startDate") String SstartDate,
                                                                  @Parameter(description = "Fecha de fin del intervalo de búsqueda", required = true) @QueryParam("endDate") String SendDate,
                                                                  @Parameter(description = "Tipo de llamada que queremos buscar en ese intervalo (OPCIONAL)", required = false) @QueryParam("type") String Stype,
                                                                  @Parameter(description = "Indice sobre el que vamos hacer la búsqueda", required = true) @QueryParam("start") String Sstart,
                                                                  @Parameter(description = "Numero de clientes que se mostrarán", required = true) @QueryParam("maxResults") String SmaxResults) throws InstanceNotFoundException, InputValidationException {
        Long customerId;
        LocalDateTime startDate, endDate;
        PhoneCallType type;
        Integer start, maxResults;

        try {
            customerId = Long.valueOf(ScustomerId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(SstartDate, formatter);
            startDate = LocalDateTime.of(date, LocalTime.ofSecondOfDay(0));
            date = LocalDate.parse(SendDate, formatter);
            endDate = LocalDateTime.of(date, LocalTime.ofSecondOfDay(0));
            if (Stype != null ){
                type = CallToCallDtoJaxbConversor.CallTypeConvertFromStr(Stype);
            }else{
                type = null;
            }
            start = Integer.valueOf(Sstart);
            maxResults = Integer.valueOf(SmaxResults);
        } catch (NumberFormatException | InputValidationException nfe) {
            throw new InputValidationException(nfe.getMessage());
        }

        final List<PhoneCall> phoneCallList = telcoService.getCallsByDateRange(customerId, startDate, endDate, type, start, maxResults);
        return CallToCallDtoJaxbConversor.toSearchedPhoneCallsDtoJaxbs(phoneCallList);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Buscar llamadas en un mes",
            description = "Buscar las llamdas de un cliente en un mes y año vencido y con el estado de las llamadas en PENDING",
            responses = {
                    @ApiResponse(responseCode  = "200", description="Devuelve las llamadas"),
                    @ApiResponse(responseCode = "400", description="Los parámetros de la petición son inválidos o incorrectos",
                            content = @Content(schema = @Schema(implementation = InputValidationException.class))),
                    @ApiResponse(responseCode = "404", description="El ID del cliente no existe",
                            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class))),
                    @ApiResponse(responseCode = "409", description="Error al buscar llamadas en un mes que todavía no ha terminado",
                            content = @Content(schema = @Schema(implementation = MonthNotExpiredException.class))),
                    @ApiResponse(responseCode = "409", description="Error al buscar llamadas en un mes que expiró pero que non están en estado PENDING",
                            content = @Content(schema = @Schema(implementation = MonthNotExpiredException.class)))})
    public List<PhoneCallsDtoJaxb> searchCallsByCustomerAndMonth(@Parameter(description = "Id del cliente que realizó las llamadas", required = true) @QueryParam("customerId") String ScustomerId,
                                                                 @Parameter(description = "Año de las llamadas que queremos buscar", required = true) @QueryParam("year") String Syear,
                                                                 @Parameter(description = "Mes de las llamadas que queremos buscar", required = true) @QueryParam("month") String Smonth) throws InstanceNotFoundException, CallsNotPendingException, MonthNotExpiredException, InputValidationException {
        Long customerId;
        Integer year, month;

        try {
            customerId = Long.valueOf(ScustomerId);
            year = Integer.valueOf(Syear);
            month = Integer.valueOf(Smonth);
        } catch (NumberFormatException e) {
            throw new InputValidationException(e.getMessage());
        }
        final List<PhoneCall> phoneCallList = telcoService.getCallsByCustomerAndMonth(customerId, year, month);
        return CallToCallDtoJaxbConversor.phoneCallsDtoJaxbs(phoneCallList);
    }
}