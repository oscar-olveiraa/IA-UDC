package es.udc.rs.telco.test.model.telcoservice;

import java.time.LocalDateTime;

import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.telcoservice.MockTelcoService;
import es.udc.rs.telco.model.telcoservice.TelcoService;
import es.udc.rs.telco.model.telcoservice.TelcoServiceFactory;
import es.udc.rs.telco.model.telcoservice.exceptions.CallsNotPendingException;
import es.udc.rs.telco.model.telcoservice.exceptions.CustomerHasCallsException;
import es.udc.rs.telco.model.telcoservice.exceptions.DifferentStateCalls;
import es.udc.rs.telco.model.telcoservice.exceptions.MonthNotExpiredException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import jdk.jfr.StackTrace;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


public class TelcoServiceTest {


    private static TelcoService telcoService = null;

    private static Customer c1 = null;
    private static Customer c2 = null;
    private static Customer c3 = null;
    private static Customer c4 = null;
    private static Customer c5 = null;
    private static Customer c6 = null;
    private static Customer c7 = null;
    private static Customer c8 = null;
    private static Customer c9 = null;


    private static PhoneCall pc1 = null;
    private static PhoneCall pc2 = null;
    private static PhoneCall pc3 = null;
    private static PhoneCall pc4 = null;
    private static PhoneCall pc5 = null;
    private static PhoneCall pc6 = null;
    private static PhoneCall pc7 = null;
    private static PhoneCall pc8 = null;
    private static PhoneCall pc9 = null;

    private void clearAll(){
        MockTelcoService mock = (MockTelcoService) telcoService;
        mock.cleanDatabase();
    }

    private List<PhoneCall>  DEBUG(Long callId, int year, int month){
        MockTelcoService mock = (MockTelcoService) telcoService;
        return mock.getCallsByCustomerAndMonthDEBUG(callId, year, month);
    }


    @BeforeAll
    public static void init() {
        telcoService = TelcoServiceFactory.getService();
        c1 = new Customer("Anastasio Docampo", "11223344D", "Calle FIC", "123456789");
        c2 = new Customer("Maria Dominguez", "55667788G", "Calle Elviña", "987654321");
        c3 = new Customer("Anastasio Docampo", "99886655K", "Calle CTIC", "124680359");
        c4 = new Customer("Anastasio Docampo", "45343535H", "Calle Vista", "45678912");
        c5 = new Customer("Jose Martinez", "56785434", "Avenida Zapateira", "231464578");
        c6 = new Customer("", "56347645N", "Avenida Castros", "456789321");
        c7 = new Customer("Anastasio Docampo", "45342456D", "Calle Locos", "954023658");
        c8 = new Customer("Anastasio Fijeira", "34235678F", "Calle Kaka", "132454335");
        c9 = new Customer("Mario Vaquerizo", "45678321K", "Calle Lola", "561232156");

    }

    @Test
    public void testAddCustomer() throws InputValidationException {
        clearAll();
        Customer add1 = telcoService.addCustomer(c1);
        Customer add2 = telcoService.addCustomer(c2);

        assertEquals(c1, add1);
        assertEquals(add1.getCustomerId(), 1);
        assertEquals(add1.getAddress(), "Calle FIC");
        assertEquals(add2.getCustomerId(), 2);
        assertEquals(add2.getName(), "Maria Dominguez");

    }

    @Test
    public void testAddCustomerException() throws InputValidationException{
        clearAll();
        //Error ya que es un usuario sin nombre
        assertThrows(InputValidationException.class, () -> {
            Customer add3 = telcoService.addCustomer(c6);
            telcoService.deleteCustomer(add3.getCustomerId());
        });

        //Error ya que es un usuario con un numero de telefono con menos de 9 digitos
        assertThrows(InputValidationException.class, () -> {
            Customer add4 = telcoService.addCustomer(c4);
            telcoService.deleteCustomer(add4.getCustomerId());
        });

        //Error ya que el DNI del usuario no tiene letra
        assertThrows(InputValidationException.class, () -> {
            Customer add5 = telcoService.addCustomer(c5);
            telcoService.deleteCustomer(add5.getCustomerId());
        });


    }

    @Test
    public void testUpdateCustomer() throws InputValidationException, InstanceNotFoundException {
        clearAll();
        Customer add1 = telcoService.addCustomer(c1);


        add1.setName("Pablo Gomez");
        add1.setAddress("Calle Constantinopla");
        telcoService.updateCustomer(add1);

        assertEquals(add1.getName(), "Pablo Gomez");
        assertEquals(add1.getAddress(), "Calle Constantinopla");
    }

    @Test
    public void testUpdateCustomerException() throws InputValidationException, InstanceNotFoundException {
        clearAll();
        Customer add2 = telcoService.addCustomer(c8);

        //Error al actualizar mal un campo del cliente
        assertThrows(InputValidationException.class, () -> {
            add2.setAddress("");
            telcoService.updateCustomer(add2);
        });

        //Error al actualizar un cliente que no existe
        assertThrows(InstanceNotFoundException.class, () -> {
            c3.setAddress("Avenida Sarmiento");
            telcoService.updateCustomer(c3);
        });
    }

    @Test
    public void testAddPhoneCall() throws InputValidationException, InstanceNotFoundException {
        clearAll();
        Customer add1 = telcoService.addCustomer(c1);


        PhoneCall pc1 = new PhoneCall(add1.getCustomerId(), LocalDateTime.of(2023, 10, 1, 12, 0),
                34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
        PhoneCall addedPc1 = telcoService.addPhoneCall(pc1);

        assertNotNull(addedPc1.getPhoneCallId());  // Verificar que la llamada tiene un ID asignado
        assertEquals(addedPc1.getCustomerId(), add1.getCustomerId());  // Verificar que la llamada esta asociada al cliente corrcto
        assertEquals(addedPc1.getDuration(), 34L);  // Verificar la duracion
        assertEquals(addedPc1.getDestinationNumber(), "666666666");  // Verificar el numero de destino
        assertEquals(addedPc1.getPhoneCallType(), PhoneCallType.LOCAL);  // Verificar el tipo de llamada
        assertEquals(addedPc1.getPhoneCallStatus(), PhoneCallStatus.PENDING);  // Verificar el estado inicail de la llamada

    }

    @Test
    public void testAddPhoneCallException() throws InputValidationException, InstanceNotFoundException {
        clearAll();
        Customer add1 = telcoService.addCustomer(c1);
        Customer add2 = telcoService.addCustomer(c2);

        // Llamada destino con menos de 9 dígitos
        PhoneCall pc2 = new PhoneCall(add1.getCustomerId(), LocalDateTime.of(2023, 10, 2, 15, 0),
                45L, "1234567", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);

        assertThrows(InputValidationException.class, () -> {
            telcoService.addPhoneCall(pc2);
        });

        // Llamada con duracion negativa
        PhoneCall pc3 = new PhoneCall(add2.getCustomerId(), LocalDateTime.of(2023, 10, 3, 14, 30),
                -15L, "987654321", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);

        assertThrows(InputValidationException.class, () -> {
            telcoService.addPhoneCall(pc3);
        });

        // Llamada con cliente no existente
        PhoneCall pc4 = new PhoneCall(c9.getCustomerId(), LocalDateTime.of(2023, 10, 4, 16, 30),
                60L, "876543210", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);

        assertThrows(InstanceNotFoundException.class, () -> {
            telcoService.addPhoneCall(pc4);
        });
    }


    @Test
    public void testDeleteCustomer() throws InputValidationException, CustomerHasCallsException, InstanceNotFoundException {
        clearAll();

        Customer add1 = telcoService.addCustomer(c1);
        Customer add2 = telcoService.addCustomer(c3);

        assertEquals(add1.getCustomerId(), 1);
        assertEquals(add2.getCustomerId(), 2);
        telcoService.deleteCustomer(add1.getCustomerId());
        assertThrows(InstanceNotFoundException.class, () -> telcoService.getCustomerById(add1.getCustomerId()));
        assertEquals(add2.getCustomerId(), 2);
    }

    @Test
    public void testDeleteCustomerException() throws InputValidationException, CustomerHasCallsException, InstanceNotFoundException {
        clearAll();

        Customer add1 = telcoService.addCustomer(c1);
        Customer add2 = telcoService.addCustomer(c3);

        //Añadimos una llamada al cliente add1
        pc1 = new PhoneCall(add1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
        PhoneCall pcadd1 = telcoService.addPhoneCall(pc1);

        //Eliminamos add2 para que salte la expcepcion al intentar borrarlo otra vez
        telcoService.deleteCustomer(add2.getCustomerId());

        //Comrpobamos que el clietne add2 se borrara
        assertThrows(InstanceNotFoundException.class, () -> {
            telcoService.deleteCustomer(add2.getCustomerId());
        });

        //Comprobamos que no se puede eliminar el usuario add1 ya que tiene llamadas hechas
        assertThrows(CustomerHasCallsException.class, () -> {
            telcoService.deleteCustomer(add1.getCustomerId());
        });
    }


    @Test
    public void testGetCallsByDateRange() {
        clearAll();

        assertThrows(InstanceNotFoundException.class, () -> {
            telcoService.getCallsByDateRange(777L, LocalDateTime.of(2024, 1, 1, 0, 0) , LocalDateTime.of(2024, 3, 1, 0, 0), null,0, 10);
        });

        try {
            //Añadir un cliente
            telcoService.addCustomer(c1);

            //Chamadas do 1 de Enero a 1 de Marzo, Local
            pc1 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
            pc2 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 2, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
            pc3 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 3, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);

            //Chamadas do 1 de Abril a 1 de Xuño, National
            pc4 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 4, 1, 0, 0), 34L, "666666666", PhoneCallType.NATIONAL, PhoneCallStatus.PENDING);
            pc5 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 5, 1, 0, 0), 34L, "666666666", PhoneCallType.NATIONAL, PhoneCallStatus.PENDING);
            pc6 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 6, 1, 0, 0), 34L, "666666666", PhoneCallType.NATIONAL, PhoneCallStatus.PENDING);

            //Chamadas do 1 de Xullo a 1 de Septiembre, International
            pc7 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 7, 1, 0, 0), 34L, "666666666", PhoneCallType.INTERNATIONAL, PhoneCallStatus.PENDING);
            pc8 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 8, 1, 0, 0), 34L, "666666666", PhoneCallType.INTERNATIONAL, PhoneCallStatus.PENDING);
            pc9 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 9, 1, 0, 0), 34L, "666666666", PhoneCallType.INTERNATIONAL, PhoneCallStatus.PENDING);

            telcoService.addPhoneCall(pc1);
            telcoService.addPhoneCall(pc2);
            telcoService.addPhoneCall(pc3);
            telcoService.addPhoneCall(pc4);
            telcoService.addPhoneCall(pc5);
            telcoService.addPhoneCall(pc6);
            telcoService.addPhoneCall(pc7);
            telcoService.addPhoneCall(pc8);
            telcoService.addPhoneCall(pc9);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Buscar chamadas entre dous rangos que non deberia salir nada
        assertTrue(() -> {
            try {
                return telcoService.getCallsByDateRange(c1.getCustomerId(), LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 2, 1, 0, 0), null,0, 10).isEmpty();
            } catch (InstanceNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            }
        });
        //Buscar chamadas entre dous rangos recollendo todo
        assertTrue(() -> {
            try {
                return telcoService.getCallsByDateRange(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 9, 1, 0, 0), null,0, 10).size() == 9;
            } catch (InstanceNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            }
        });
        //Buscar chamadas entre dous rangos recollendo con start distinto e max results distinto
        assertTrue(() -> {
            try {
                List<PhoneCall> calls = telcoService.getCallsByDateRange(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 9, 1, 0, 0), null,2, 4);
                return calls.size() == 4 && calls.getFirst().getPhoneCallId().equals(pc3.getPhoneCallId());
            } catch (InstanceNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            }
        });

        //Probando con phonecalltype
        assertTrue(() -> {
            try {
                List<PhoneCall> calls = telcoService.getCallsByDateRange(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 9, 1, 0, 0),PhoneCallType.INTERNATIONAL, 0, 10);
                return calls.size() == 3 && calls.getFirst().getPhoneCallId().equals(pc7.getPhoneCallId());
            } catch (InstanceNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            }
        });

        clearAll();
    }

    @Test
    public void testGetCallsByCustomerAndMonth() {
        clearAll();

        try{
            telcoService.addCustomer(c1);

            pc1 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
            pc2 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
            pc3 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 2, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
            pc4 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 2, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);

            telcoService.addPhoneCall(pc1);
            telcoService.addPhoneCall(pc2);

            telcoService.addPhoneCall(pc3);
            telcoService.updateCallStatus(c1.getCustomerId(),2023,2,PhoneCallStatus.BILLED);
            telcoService.addPhoneCall(pc4);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        assertTrue(() -> {
            try {
                return telcoService.getCallsByCustomerAndMonth(c1.getCustomerId(), 2023, 1).size() == 2;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return false;
        });

        clearAll();
    }


    @Test
    public void testGetCallsByCustomerAndMonthExceptions() throws InstanceNotFoundException, DifferentStateCalls, MonthNotExpiredException, InputValidationException {
        clearAll();
        telcoService.addCustomer(c1);

        pc1 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
        pc2 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
        pc3 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 2, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
        pc4 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 2, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);

        telcoService.addPhoneCall(pc1);
        telcoService.addPhoneCall(pc2);

        telcoService.addPhoneCall(pc3);
        telcoService.updateCallStatus(c1.getCustomerId(),2023,2,PhoneCallStatus.BILLED);
        telcoService.addPhoneCall(pc4);

        //El mes aún no ha expirado
        assertThrows(MonthNotExpiredException.class, () -> {
            telcoService.getCallsByCustomerAndMonth(c1.getCustomerId(), 2024, 12);
        });

        //El cliente no existe
        assertThrows(InstanceNotFoundException.class, () -> {
            telcoService.getCallsByCustomerAndMonth(777L, 2023, 1);
        });

        //El cliente no tiene llamadas en estado Pending
        assertThrows(CallsNotPendingException.class, () -> {
            telcoService.getCallsByCustomerAndMonth(c1.getCustomerId(), 2023, 2);
        });
    }


    @Test
    public void testUpdateCallStatus() {

        clearAll();

        try {
            telcoService.addCustomer(c1);
            telcoService.addCustomer(c2);
            telcoService.addCustomer(c3);

            pc1 = new PhoneCall(c1.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
            pc2 = new PhoneCall(c2.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);
            pc3 = new PhoneCall(c3.getCustomerId(), LocalDateTime.of(2023, 1, 1, 0, 0), 34L, "666666666", PhoneCallType.LOCAL, PhoneCallStatus.PENDING);


            telcoService.addPhoneCall(pc1);
            telcoService.addPhoneCall(pc2);
            telcoService.addPhoneCall(pc3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertThrows(MonthNotExpiredException.class, () -> {
            telcoService.updateCallStatus(c1.getCustomerId(), 2024, 12, PhoneCallStatus.BILLED);
        });
        assertThrows(InstanceNotFoundException.class, () -> {
            telcoService.updateCallStatus(777L, 2023, 1, PhoneCallStatus.BILLED);
        });
        assertTrue(() -> {
            try {
                telcoService.updateCallStatus(c1.getCustomerId(), 2023, 1, PhoneCallStatus.BILLED);
                for (PhoneCall phoneCall : DEBUG(c1.getCustomerId(), 2023, 1)) {
                    if (phoneCall.getPhoneCallStatus() == PhoneCallStatus.PENDING) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertTrue(() -> {
            try {
                telcoService.updateCallStatus(c1.getCustomerId(), 2023, 1, PhoneCallStatus.BILLED);
                List<PhoneCall> before = DEBUG(c1.getCustomerId(), 2023, 1);
                telcoService.updateCallStatus(c1.getCustomerId(), 2023, 1, PhoneCallStatus.BILLED);
                List<PhoneCall> after = DEBUG(c1.getCustomerId(), 2023, 1);
                for (int i = 0; i < before.size(); i++) {
                    if (before.get(i).getPhoneCallStatus() != after.get(i).getPhoneCallStatus()) return false;
                }
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertTrue(() -> {
            try {
                telcoService.updateCallStatus(c1.getCustomerId(), 2023, 1, PhoneCallStatus.PAID);
                for (PhoneCall phoneCall : DEBUG(c1.getCustomerId(), 2023, 1)) {
                    if (phoneCall.getPhoneCallStatus() != PhoneCallStatus.PAID) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        clearAll();
    }

    // Prueba para buscar un cliente por su DNI
    @Test
    public void testGetCustomerByDNI() throws InstanceNotFoundException, InputValidationException {
        clearAll();

        Customer add1 = telcoService.addCustomer(c1);

        Customer foundCustomer = telcoService.getCustomerByDNI("11223344D");

        assertEquals(add1.getName(), foundCustomer.getName(), "El nombre del cliente no coincide");
        assertEquals(add1.getDni(), foundCustomer.getDni(), "El DNI del cliente no coincide");
    }


    @Test
    public void testGetCustomerByDNINotFound() {
        // Intentar recuperar un cliente con un DNI no existente y verificar la excepción
        assertThrows(InstanceNotFoundException.class, () -> {
            telcoService.getCustomerByDNI("99999999C");
        }, "Se esperaba una excepción al buscar un DNI inexistente");
    }

    // Prueba para recuperar un cliente por su ID
    @Test
    public void testGetCustomerById() throws InstanceNotFoundException, InputValidationException{
        clearAll();
        Customer add1 = telcoService.addCustomer(c1);
        // Intentar recuperar el cliente usando su ID
        Customer foundCustomer = telcoService.getCustomerById(1L);

        // Verificar que el cliente recuperado sea el mismo que el agregado
        assertEquals(add1.getName(), foundCustomer.getName(), "El nombre del cliente no coincide");
        assertEquals(add1.getCustomerId(), 1L, "El ID del cliente no coincide");
    }

    // Prueba que lanza InputValidationException al buscar un cliente con un ID no existente
    @Test
    public void testGetCustomerByIdNotFound() {
        // Intentar recuperar un cliente con un ID no existente y verificar la excepción
        assertThrows(InstanceNotFoundException.class, () -> {
            telcoService.getCustomerById(99L); // Asumiendo que 99L no existe
        }, "Se esperaba una excepción al buscar un ID inexistente");
    }


    @Test
    public void testSearchCustomersByName() throws InputValidationException {
        clearAll();

        // Agregamos clientes de prueba
        telcoService.addCustomer(c3);
        telcoService.addCustomer(c7);

        // Buscamos clientes con el nombre "Anastasio Docampo"
        List<Customer> result = telcoService.searchCustomersByName("Anastasio Docampo", 0, 10);

        // Verificamos que se hayan encontrado dos clientes
        assertEquals(2, result.size(), "Se esperaban 2 clientes con el nombre 'Anastasio Docampo'");

        // Verificamos que ambos clientes tienen el nombre correcto
        for (Customer customer : result) {
            assertEquals("Anastasio Docampo", customer.getName(), "El nombre del cliente no coincide");
        }
    }


    // Prueba que lanza InputValidationException al buscar con parámetros inválidos
    @Test
    public void testSearchCustomersByNameInvalidParams() {
        assertThrows(InputValidationException.class, () -> {
            telcoService.searchCustomersByName("", 0, 10);
        }, "Se esperaba una excepción al buscar con un texto vacío");
    }
}
