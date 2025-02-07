# Memoria justificativa del proyecto
---------------------------------------------------------------------

## Iteración 1
---------------------------------------------------------------------

### Pruebas WS-BPEL
 - URL a los documentos WSDL que es necesario utilizar:  
   
    http://localhost:5050/CompositeApp1/getCallsInfo?wsdl

 - Nombre del fichero SoapUI con las peticiones: 

     flow-practica-ia-soapui-project.xml

### Justificaciones de diseño

-  Diseño capa modelo:
 
   - En la clase **Customer** donde definimos el cliente, hemos creado un constructor si el campo id
     ya que a la hora de crar el usuario, no se le asigna un id hasta que se añada a la lista
   
   - En la clase **PhoneCall** donde definimos las llamadas, hemos asignado en su constructor, que
     el estado de la llamada al ser creada, su estado sea automaticamente PENDING, 
     resolviendo asi posibles conflictos en la lógica del programa
   
   - En la clase **MockTelcoService** donde definimos la capa modelo, hemos creado una serie de métodos auxiliares
   para la validación de atributos:
   
       1) Validación DNI (mirar que tenga 8 digitos y una letra)
       2) Mirar si existe un usuario (si un usuario ya existe no tiene sentido añadirlo)
       3) Validar el teléfono del usuario (mirar que tenga 9 dígitos solamente, nosotros no tenemos en cuenta las extensiones de llamada)
       4) Validar que el mes no expirara
       5) Validar un usuario (mirar que al insertar un usuario tenga DNI, nombre, telefono y direccion)
       6) Validar las llamadas (ningun campo puede ser null, o la duracion negativa o validar numero de telefono)
           
   - En la clase **MockTelcoService**, hemos creado un método para el borrado total de las listas una vez que
   la ejecución de ese test finalizó y así iniciar el siguiente test con la BD embebida vacía
    
   - En la clase de los tests, inicializamos los tests con los usuarios creados pero no insertados 
   
   - **Excepciones Personalizadas para Manejo de Errores**: Se han definido excepciones específicas (como `CustomerHasCallsException`, `CallsNotPendingException` y `MonthExpiredException`) 
      para identificar y manejar errores en cada operación crítica. Esto asegura una lógica de negocio más robusta y mejora la experiencia del usuario al proporcionar mensajes de error claros.
### Problemas Conocidos en el Diseño / Implementación de la Práctica
- **Conflictos en el Repositorio GIT**: La división del trabajo entre los tres miembros del equipo generó conflictos al integrar cambios en el repositorio. Esto requirió una cuidadosa gestión de las fusiones y una comunicación constante para mantener la coherencia en el código.

- **Sincronización de la Lógica en Múltiples Operaciones**: Durante la implementación, surgieron problemas al integrar las distintas funciones del sistema, especialmente cuando las operaciones requerían datos compartidos entre varios métodos. Esto se solucionó con un enfoque de pruebas exhaustivas y revisiones cruzadas entre los miembros del equipo.

- **Ajustes en la Validación y Gestión de Errores**: Durante el desarrollo, fue necesario ajustar y depurar las validaciones y el manejo de excepciones para capturar correctamente los errores en casos específicos. Esto demandó una revisión minuciosa de los casos límite y pruebas adicionales para garantizar su correcto funcionamiento.

- **Desafíos en el Flujo de WS-BPEL**: La definición del flujo de trabajo en WS-BPEL presentó retos significativos, especialmente en la coordinación de los servicios web involucrados. Se identificaron dificultades para asegurar que las respuestas de los servicios se procesaran en el orden correcto, lo que llevó a inconsistencias en el flujo de datos. Se realizaron revisiones exhaustivas de las definiciones de procesos y ajustes en los puntos de sincronización para resolver estos problemas.

- **Problemas de Despliegue de Servicios**: Durante el despliegue de los servicios web necesarios para la implementación de WS-BPEL, se encontraron problemas relacionados con la configuración del entorno de ejecución. A veces, el servicio no se desplegó correctamente debido a dependencias no resueltas o configuraciones incorrectas en el archivo de despliegue, lo que generó retrasos en las pruebas.

- **Fallos de Puertos Ocupados**: Se experimentaron fallos ocasionados por puertos ocupados al intentar iniciar los servicios locales. Esto causó que algunos servicios no pudieran iniciarse, resultando en errores de conexión durante las pruebas. Se implementó una estrategia de gestión de puertos para evitar conflictos, incluyendo la asignación de puertos específicos y la verificación de que no estuvieran en uso antes de iniciar el entorno de prueba.

- **Excepciones en el BPEL**: las excepciones en la orquestación de los servicios los manejamos pero en, la peticion al servidor, no se devuelve exactamente la excepción que se lanzó

- **Fallos servidor BPEL**: cuando iniciamos por primera vez el servidor BPEL, algunas veces nos salta un error entonces tenemos que volver a compilar y hacer 'deploy' del flujo para que se arregle
### Resumen de Contribución de Cada Miembro del Grupo (Consistente con Commits en Repositorio GIT)
- **Óscar**: Responsable de las operaciones de gestión de clientes, incluyendo `addCustomer`, `updateCustomer`, `deleteCustomer` y `addPhoneCall`. También desarrolló pruebas unitarias para estas funcionalidades y colaboró en la optimización de la lógica asociada a la gestión de datos de clientes y llamadas. Sobre el flujo BPEL, importó los archivos necesarios como wsdl (forma abstrata y esquemas) y colaboró tanto en el manejo de excepciones como en la correción de errores que se mostraban al hacer la petición en el SoapUI

- **Yago**: Encargado de las funciones de recuperación de datos de clientes, desarrollando `getCustomerByDNI`, `getCustomerByID` y `searchCustomerByName`. Además, Yago implementó pruebas unitarias para estas operaciones y documentó los métodos para mejorar la claridad y comprensión del código. Sobre el flujo BPEL, creó el composite y la conexión del módulo bpel con los servicios, asignando manualmente las URL (ya que se hizo de forma abstrata). También colaboró en diseño del flujo haciendo la idea principal y en el manejo de excepciones

- **Eloy**: Responsable de las operaciones relacionadas con la gestión de llamadas y sus estados. Implementó `getCallsByCustomerAndMonth`, `updateCallStatus` y `getCallsByDateRange`, además de sus respectivas pruebas unitarias, optimizando también la organización del código en estas áreas para mejorar su eficiencia y claridad. Sobre el flujo BPEL, creo el flujo de los servicios siguiendo el esquema realizado anteriormente y colaboró en el manejo de excepciones


## Iteración 2
---------------------------------------------------------------------

### Partes opcionales incluidas y miembros del grupo que han participado
- Documentación del servicio con Swagger(fichero [Documentacion rs-telco.postman_collection.json](Documentacion%20rs-telco.postman_collection.json) (Óscar Olveira Miniño)
- Github Copilot (Yago García Araújo) [Prompts Github Copilot.md](Prompts%20Copilot%20con%20salida.md)

### Pruebas WS REST
- Nombre del fichero SoapUI/colección Postman con las peticiones a probar:

  [Defensa IA.postman_collection.json](Defensa%20IA.postman_collection.json)

- Comandos maven necesarios para ejecutar las pruebas

  [comandos_maven](comandos_maven.md)

### Pruebas WS-BPEL
- URL a los documentos WSDL que es necesario utilizar:

  http://localhost:5050/CompositeApp1/getCallsInfo?wsdl

- Nombre del fichero SoapUI con las peticiones:

  [flow-practica-ia-soapui-project.xml](flow-practica-ia-soapui-project.xml)
- 
### Problemas Conocidos en el Diseño/Implementación de la Práctica

  - **Conflictos en el Repositorio GIT:** La colaboración entre los tres miembros del equipo generó conflictos en la integración de cambios en el repositorio. Esto requirió revisiones manuales y fusiones cuidadosas, además de mantener una comunicación constante para garantizar la coherencia en el código y la alineación con los objetivos del proyecto.

  - **Sincronización de la Lógica en Múltiples Operaciones:** Al integrar las funciones del sistema, surgieron problemas relacionados con el uso de datos compartidos entre varios métodos. Por ejemplo, la validación previa en la capa cliente a veces no coincidía con las restricciones establecidas en la capa de servicios, lo que resultaba en solicitudes rechazadas inesperadamente. Esto se solucionó con pruebas exhaustivas y ajustes iterativos en las validaciones.

  - **Inconsistencias en las Validaciones del Modelo:** Aunque la capa modelo incluye métodos para validar datos como DNI, teléfonos y usuarios, se encontraron casos límite en los que las validaciones permitieron datos incorrectos. Por ejemplo, no siempre se detectaron correctamente caracteres inválidos en el DNI o formatos de teléfono poco comunes. Esto llevó a un esfuerzo adicional para refinar las reglas de validación.

  - **Gestión de Excepciones en Servicios y Cliente:** La implementación de excepciones personalizadas mejoró la claridad, pero se identificaron problemas cuando las excepciones generadas en la capa de servicios no se transmitían correctamente al cliente. En algunos casos, el cliente recibió respuestas genéricas en lugar de mensajes específicos, dificultando la depuración y la experiencia del usuario.

  - **Fallos en el Parsing de Peticiones por Línea de Comandos:** Aunque el cliente está diseñado para aceptar peticiones desde la línea de comandos, se encontraron errores en el análisis (*parsing*) de ciertos comandos complejos, especialmente cuando los usuarios omitían parámetros obligatorios o usaban formatos incorrectos. Esto resultó en operaciones fallidas y mensajes de error ambiguos.

  - **Desafíos en el Diseño Modular de Servicios:** A pesar de la modularidad diseñada para los servicios, surgieron dependencias implícitas entre algunas operaciones, lo que dificultó la ampliación del sistema. Por ejemplo, agregar una nueva funcionalidad en la gestión de llamadas requería modificaciones inesperadas en otros servicios, rompiendo parcialmente el principio de encapsulamiento.

  - **Problemas de Desempeño en la Capa de Servicios:** Aunque se optimizaron las operaciones, se identificaron tiempos de respuesta elevados en escenarios con múltiples llamadas concurrentes, especialmente en validaciones complejas o al procesar datos con inconsistencias. Esto afectó la experiencia del usuario en entornos de prueba con alta carga.

  - **Problemas en el Reinicio de la Base de Datos Embebida:** Aunque se creó un método para limpiar las listas de datos en la capa modelo tras finalizar cada prueba, se detectaron errores intermitentes donde los datos residuales no se eliminaban completamente. Esto llevó a resultados inconsistentes en las pruebas subsecuentes.

  - **Dificultades con Excepciones en WS-BPEL:** Las excepciones manejadas dentro del flujo WS-BPEL no siempre se reflejaron correctamente en las respuestas SOAP. Esto generó problemas al depurar errores, ya que el cliente no siempre podía distinguir entre un fallo controlado y un error inesperado.

  - **Problemas de Compatibilidad con Configuraciones de Entorno:** La configuración de servicios, como las URLs y los puertos en el flujo WS-BPEL y el cliente, a veces no se actualizaba correctamente al cambiar de entorno (pruebas locales a producción), lo que provocaba errores de conexión.

  - **Errores Intermitentes en el Despliegue de Servicios:** Durante el despliegue, algunos servicios no se registraban correctamente debido a configuraciones incompletas o dependencias no resueltas. Esto afectó especialmente al flujo WS-BPEL, requiriendo múltiples intentos de compilación y despliegue.

  - **Excepciones no Controladas en la Capa Cliente:** A pesar de las validaciones previas en la capa cliente, hubo casos donde excepciones no controladas interrumpieron el flujo de ejecución. Estas excepciones ocurrieron cuando se intentó procesar respuestas inesperadas de los servicios o al manejar datos incompletos proporcionados por el usuario.

  - **Dependencia en Pruebas Manuales:** Aunque se implementaron pruebas automatizadas para varias operaciones, algunas validaciones y escenarios límite dependieron de pruebas manuales, lo que introdujo variabilidad en los resultados y aumentó el tiempo necesario para garantizar la calidad del sistema.



### Resumen de Contribución de Cada Miembro del Grupo (Consistente con Commits en Repositorio GIT)

- **Óscar**: Corrigió errores en la capa modelo y separó los tests para mejorar la organización y claridad del código. En la capa de servicios, desarrolló las funcionalidades de **añadir cliente**, **buscar cliente por ID**, **buscar cliente por DNI**, **eliminar cliente**, **cambiar el estado de una llamada** y **crear llamada**. En la capa cliente, corrigió errores introducidos durante la implementación y diseñó la funcionalidad para **añadir cliente**, asegurando un flujo coherente y robusto.

- **Yago**: Implementó la funcionalidad de **búsqueda de clientes por texto contenido** y la operación de **actualizar cliente** en la capa de servicios. En la capa cliente, diseñó la funcionalidad para **crear llamada** y **eliminar cliente**, además de modificar partes de la implementación en otras capas cuando se detectaron problemas que afectaban el comportamiento global del sistema.

- **Eloy**: En la capa de servicios, desarrolló las funcionalidades de **búsquedas de llamadas de un cliente entre dos fechas**, **cambiar el estado de llamadas de un mes** y **buscar llamadas por un mes concreto**. En la capa cliente, corrigió diversos errores para garantizar que las operaciones se ejecutaran correctamente y mejorar la interacción entre cliente y servicios.