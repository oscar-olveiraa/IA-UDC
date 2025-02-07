### 1. **Creación de alguna de las operaciones del servicio del modelo que proporcionará las funcionalidades de la aplicación.**

> "Crea un método en Java para agregar un nuevo cliente a un sistema. El método debe recibir los datos del cliente, como el nombre, los detalles de contacto y cualquier otra información relevante. Debe validar y comprobar los datos del cliente antes de guardarlos en la base de datos. Si los datos no son válidos, el método debe lanzar una excepción personalizada `ClienteInvalidoException`. El método debe devolver un `customerId` único de tipo `Long`. Asegúrate de manejar adecuadamente las excepciones, registrar la operación y proporcionar comentarios detallados que expliquen la lógica del código."

```java
public class ClienteService {

    public Long agregarCliente(String nombre, String detallesContacto, String otraInfo) throws ClienteInvalidoException {
        if (nombre == null || nombre.isEmpty() || detallesContacto == null || detallesContacto.isEmpty()) {
            throw new ClienteInvalidoException("Datos del cliente no válidos");
        }

        // Validar y comprobar otros datos del cliente según sea necesario

        // Simulación de guardar en la base de datos y generar un customerId único
        Long customerId = System.currentTimeMillis(); // Ejemplo de generación de ID único

        // Registrar la operación
        System.out.println("Cliente agregado con ID: " + customerId);

        return customerId;
    }
}

class ClienteInvalidoException extends Exception {
    public ClienteInvalidoException(String mensaje) {
        super(mensaje);
    }
}
```

### 2. **Creación de pruebas automatizadas para alguna operación de los servicios del modelo.**

> "Genera pruebas unitarias utilizando JUnit para un método que agrega un nuevo registro de llamada telefónica al sistema. El método debe aceptar parámetros como `customerId`, `startTime`, `duration`, `destinationNumber` y `callType`. Las pruebas deben cubrir escenarios normales, así como casos extremos, como un formato inválido para `startTime`, una duración negativa para la llamada, parámetros faltantes o errores inesperados de la base de datos. Incluye objetos simulados (mocks), aserciones y nombres descriptivos para las pruebas. Asegúrate de cubrir todas las ramas de la lógica."

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LlamadaServiceTest {

    private LlamadaService llamadaService;

    @BeforeEach
    public void setUp() {
        llamadaService = new LlamadaService();
    }

    @Test
    public void testAgregarLlamadaExito() {
        Long customerId = 1L;
        String startTime = "2024-12-09T10:15:30";
        int duration = 120;
        String destinationNumber = "123456789";
        String callType = "INTERNACIONAL";

        assertDoesNotThrow(() -> llamadaService.agregarLlamada(customerId, startTime, duration, destinationNumber, callType));
    }

    @Test
    public void testAgregarLlamadaFormatoInvalido() {
        Long customerId = 1L;
        String startTime = "fecha-invalida";
        int duration = 120;
        String destinationNumber = "123456789";
        String callType = "INTERNACIONAL";

        assertThrows(DateTimeParseException.class, () -> llamadaService.agregarLlamada(customerId, startTime, duration, destinationNumber, callType));
    }

    @Test
    public void testAgregarLlamadaDuracionNegativa() {
        Long customerId = 1L;
        String startTime = "2024-12-09T10:15:30";
        int duration = -10;
        String destinationNumber = "123456789";
        String callType = "INTERNACIONAL";

        assertThrows(IllegalArgumentException.class, () -> llamadaService.agregarLlamada(customerId, startTime, duration, destinationNumber, callType));
    }

    // Más pruebas para otros casos extremos y errores de base de datos
}
```

### 3. **Creación de algún recurso Jakarta Restful Web Services que ponga accesible parte de las funcionalidades del modelo de forma remota.**

> "Crea un servicio RESTful utilizando Jakarta que exponga un endpoint `GET` para recuperar los detalles de un cliente por su `customerId`. El servicio debe devolver la información del cliente en formato JSON. Implementa manejo adecuado de errores para `customerId` no válido, devolviendo un código de estado HTTP 400 para solicitudes incorrectas y 404 si el cliente no se encuentra. Asegúrate de aplicar validación de entrada y de implementar seguridad mediante autenticación/autorización. Incluye comentarios detallados que expliquen la lógica del servicio y su funcionamiento."

```java
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientes")
public class ClienteResource {

    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCliente(@PathParam("customerId") Long customerId) {
        if (customerId == null || customerId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID de cliente no válido").build();
        }

        Cliente cliente = buscarClientePorId(customerId);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado").build();
        }

        return Response.ok(cliente).build();
    }

    private Cliente buscarClientePorId(Long customerId) {
        // Simulación de búsqueda en la base de datos
        return new Cliente(customerId, "Nombre Ejemplo", "Detalles de Contacto");
    }
}

class Cliente {
    private Long id;
    private String nombre;
    private String detallesContacto;

    // Constructor, getters y setters
    public Cliente(Long id, String nombre, String detallesContacto) {
        this.id = id;
        this.nombre = nombre;
        this.detallesContacto = detallesContacto;
    }

    // Getters y setters
}
```

### 4. **Creación de alguno de los métodos del cliente de línea de comandos para cada una de las funcionalidades que la aplicación expone remotamente.**

> "Crea un método en Java para una interfaz de línea de comandos (CLI) que interactúe con un servicio RESTful para obtener los detalles de un cliente por su `customerId`. El método debe recibir el `customerId` como un argumento de línea de comandos, llamar al API REST y mostrar la información del cliente o un mensaje de error en la consola. Utiliza la clase `HttpURLConnection` o una biblioteca como `HttpClient` para realizar la solicitud HTTP. Asegúrate de manejar posibles errores y proporcionar comentarios detallados explicando el código."

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteCLI {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java ClienteCLI <opción> <argumentos>");
            return;
        }

        if ("-addCustomer".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[]{});

            // [-addCustomer] TelcoServiceClient -addCustomer <name> <DNI> <address> <phone>

            try {
                Long customerId = clientTelcoService.addCustomer(new ClientCustomerDto(null, args[1], args[2],
                        args[3], args[4]));
                System.out.println("Customer " + customerId + " created successfully");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-getCustomer".equalsIgnoreCase(args[0])) {
            Long customerId = Long.parseLong(args[1]);
            obtenerDetallesCliente(customerId);
        } else {
            System.out.println("Opción no válida");
        }
    }

    public static void obtenerDetallesCliente(Long customerId) {
        try {
            URL url = new URL("http://localhost:8080/clientes/" + customerId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Detalles del cliente: " + response.toString());
            } else if (responseCode == 400) {
                System.out.println("Solicitud incorrecta: ID de cliente no válido");
            } else if (responseCode == 404) {
                System.out.println("Cliente no encontrado");
            } else {
                System.out.println("Error inesperado: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validateArgs(String[] args, int expectedLength, int[] optionalIndexes) {
        if (args.length < expectedLength) {
            throw new IllegalArgumentException("Número de argumentos inválido");
        }
    }
}
```

### 5. **Creación de la documentación Javadoc de parte del código desarrollado.**

> "Genera comentarios Javadoc para un método que actualiza el estado de una llamada telefónica en un sistema. El método debe recibir parámetros como `callId` y el `newStatus`. 
Los comentarios Javadoc deben describir claramente el propósito del método, los parámetros que recibe, el valor de retorno y las excepciones que puede lanzar, como `EstadoLlamadaInvalidoException`. 
Asegúrate de que los comentarios sean claros y precisos, y que proporcionen una explicación detallada de cómo funciona el método."

```java
/**
 * Actualiza el estado de una llamada telefónica en el sistema.
 *
 * @param callId El identificador único de la llamada.
 * @param newStatus El nuevo estado que se asignará a la llamada.
 * @return true si el estado de la llamada se actualizó correctamente, false en caso contrario.
 * @throws EstadoLlamadaInvalidoException si el nuevo estado proporcionado no es válido.
 */
public boolean actualizarEstadoLlamada(String callId, String newStatus) throws EstadoLlamadaInvalidoException {
    // Implementación del método
}
```


El desarrollo de estas funcionalidades es fundamental para crear una aplicación sólida y eficiente. Desde la implementación de operaciones del servicio del modelo hasta la creación de pruebas automatizadas y la exposición de servicios a través de APIs RESTful, cada paso es crucial para asegurar la calidad y funcionalidad del sistema.

1. **Operaciones del Servicio del Modelo**: Crear métodos para agregar nuevos clientes y actualizar estados de llamadas implica validar los datos y manejar excepciones. Esto garantiza que solo se almacenen datos válidos en la base de datos, manteniendo la integridad del sistema.

2. **Pruebas Automatizadas**: Utilizar JUnit para pruebas unitarias permite verificar que los métodos funcionen correctamente en diferentes escenarios, incluyendo casos extremos. Esto es esencial para detectar y corregir errores antes de que afecten a los usuarios.

3. **Recursos RESTful**: Implementar servicios RESTful con Jakarta facilita la interacción remota con el sistema, permitiendo a los clientes acceder a la información de manera segura y eficiente. El manejo adecuado de errores y la implementación de autenticación y autorización son claves para proteger los datos y garantizar un acceso controlado.

4. **Métodos del Cliente de Línea de Comandos**: Proporcionar una interfaz CLI para interactuar con los servicios RESTful añade una capa de accesibilidad y conveniencia, permitiendo a los usuarios realizar operaciones directamente desde la línea de comandos. Esto es especialmente útil para administradores y desarrolladores que necesitan realizar tareas rápidamente.

5. **Documentación Javadoc**: Crear comentarios Javadoc detallados es vital para mantener un código bien documentado y fácil de entender. Esto no solo ayuda a los desarrolladores actuales, sino también a futuros mantenedores del código, facilitando la colaboración y el mantenimiento del sistema.
