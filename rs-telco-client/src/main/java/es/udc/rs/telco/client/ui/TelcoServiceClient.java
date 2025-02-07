package es.udc.rs.telco.client.ui;

import es.udc.rs.telco.client.service.ClientCustomerDto;
import es.udc.rs.telco.client.service.ClientTelcoService;
import es.udc.rs.telco.client.service.ClientTelcoServiceFactory;
import es.udc.rs.telco.client.service.ClientPhoneCallDto;
import es.udc.rs.telco.client.service.util.PhoneCallDtoToPhoneCallDtoJaxbConversor;
import es.udc.ws.util.exceptions.InputValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TelcoServiceClient {

    public static void main(String[] args) {

        if (args.length == 0) {
            printUsageAndExit();
        }
        ClientTelcoService clientTelcoService = ClientTelcoServiceFactory.getService();

        if ("-addCustomer".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[]{});

            // [-addCustomer] TelcoServiceClient -addCustomer <name> <DNI> <address> <phone>

            try {
                Long customerId = clientTelcoService.addCustomer(new ClientCustomerDto(null, args[1], args[2],
                        args[3], args[4]));
                System.out.println("Customer " + customerId + " " + "created sucessfully");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-addPhoneCall".equalsIgnoreCase(args[0])) {

            validateArgs(args, 6, new int[]{1,3});

            try {

                if (!isValidDate(args[2])) {
                    throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd HH:mm:ss.");
                }
                // Crear el objeto ClientPhoneCallDto a partir de los argumentos
                Long callId = clientTelcoService.addPhoneCall(new ClientPhoneCallDto(
                        Long.valueOf(args[1]),
                        PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorLocalDatefromString(args[2]), // startTime (String)
                        Long.valueOf(args[3]), // duration (Long)
                        args[4],             // destinationNumber (String)
                        args[5]
                ));


                // Imprimir el ID de la llamada añadida
                System.out.println("Phone call added successfully with ID: " + callId);

            } catch (Exception ex) {
                ex.printStackTrace(System.err);  // Capturar y mostrar errores
            }
        } else if ("-deleteClient".equalsIgnoreCase(args[0])) {

            validateArgs(args, 2, new int[]{});

            try {
                // Llamar al método deleteClient con el customerId proporcionado
                clientTelcoService.deleteClient(Long.valueOf(args[1])); // args[1] es el customerId
                System.out.println("Customer with ID " + args[1] + " deleted successfully.");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);  // Capturar y mostrar errores
            }
        }

        else if ("-changeCallStatus".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[]{1});
            try {
                clientTelcoService.changeCallStatus(Long.valueOf(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),args[4]);
                System.out.println("Call status changed successfully");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-searchCallsByDateRange".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[]{1,4,5});
            try {
                List<ClientPhoneCallDto> phoneCalls;
                if (!isValidDate(args[2]) || !isValidDate(args[3])) {
                    throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd HH:mm:ss.");
                }
                if (args.length > 6)
                    phoneCalls = clientTelcoService.searchCallsByDateRange(Long.valueOf(args[1]),PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorLocalDatefromString(args[2]),PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorLocalDatefromString(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6]);
                else
                    phoneCalls = clientTelcoService.searchCallsByDateRange(Long.valueOf(args[1]),PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorLocalDatefromString(args[2]), PhoneCallDtoToPhoneCallDtoJaxbConversor.conversorLocalDatefromString(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]),null);
                System.out.println("Found " + phoneCalls.size() + " phoneCall(s) for the customer '" + args[1] + "' between '"+ args[2] +"' and '"+ args[3] +"'.");
                for (ClientPhoneCallDto phoneCallDto : phoneCalls) {
                    System.out.println("Start time: " + phoneCallDto.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ", Duration: " + (phoneCallDto.getDuration()+ " m")
                            + ", Destination number: " + phoneCallDto.getDestinationNumber());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else {System.err.println("Error: unknown command");validateArgs(args, -1, new int[]{1});}
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if (expectedArgs > args.length) {
            printUsageAndExit();
        }
        for (int i = 0; i < numericArguments.length; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    private static boolean isValidDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setLenient(false); // No permitir fechas aproximadas
            format.parse(date); // Intentar analizar la fecha
            return true;} catch (ParseException e) {
            return false; // Si falla, el formato es incorrecto
            }
    }


    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println(
                "Usage:\n" +
                        "    [-addCustomer]        TelcoServiceClient -addCustomer <name> <DNI> <address> <phoneNumber>\n" +
                        "    [-changeCallStatus]   TelcoServiceClient -changeCallStatus <customerId> <month> <year> <newStatus>\n" +
                        "    [-searchCallsByDateRange] TelcoServiceClient -searchCallsByDateRange <customerId> <startDate as YYYY-MM-DD> <endDate as YYYY-MM-DD> <start> <maxResults> [<type>]\n" +
                        "    [-deleteClient]        TelcoServiceClient -deleteClient <customerId>\n" +
                        "    [-addPhoneCall]       TelcoServiceClient -addPhoneCall <startTime> <duration> <destinationNumber> <type>\n" +
                "...");
    }


}
