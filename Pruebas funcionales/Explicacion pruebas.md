# Pruebas Funcionales

Esta sección presenta las pruebas realizadas para verificar que los cambios implementados en la arquitectura del sistema funcionan correctamente y no afectan el funcionamiento general de la aplicación.

---

# Prueba 1 — Validación de campo obligatorio en Iglesia

Se verificó que el sistema no permite registrar una iglesia cuando el campo **Nombre** está vacío.  
Esta validación se implementó utilizando **Bean Validation** mediante la anotación `@NotBlank`.

La interfaz bloquea el botón **Guardar** hasta que el usuario ingrese un nombre válido, evitando el envío de datos inválidos al backend.

**Evidencia**

```java
public record ChurchRequest(
    @NotBlank(message = "El nombre es obligatorio") String name,
    String address
) {}
# Prueba 2 — Registro correcto de Iglesia

Se comprobó que el sistema permite registrar una iglesia correctamente cuando se ingresan los datos requeridos.

El backend procesa la solicitud mediante el endpoint `/api/church`, guardando la información en la base de datos y mostrando los datos registrados en la interfaz.

Esta prueba confirma que la lógica del controlador y las validaciones funcionan correctamente.

**Evidencia**

@PreAuthorize("hasRole('ADMIN')")
@PostMapping
public ChurchResponse create(@Valid @RequestBody ChurchRequest request) {
    if (churchRepository.count() > 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una iglesia registrada");
    }

    Church church = new Church();
    church.setName(request.name());
    church.setAddress(request.address());
    churchRepository.save(church);

    return ChurchResponse.from(church);
}---

# Prueba 3 — Funcionamiento del módulo de Pagos

Se accedió al módulo **Pagos** desde el menú lateral de la aplicación.

El sistema cargó correctamente la vista de gestión de pagos.  
En este caso, como no existen pagos registrados en la base de datos, el sistema muestra el mensaje **"Sin pagos"**.

Esto confirma que el módulo funciona correctamente después de los cambios implementados en `PaymentService` y `PaymentResponseDto`.

**Evidencia**

public record PaymentResponseDto(
    Long id,
    String type,
    String status,
    String amount,
    int attempts,
    Long referenceId
) {}---

# Prueba 4 — Implementación de PaymentService

Se verificó la implementación de la clase `PaymentService`, la cual encapsula la lógica de negocio relacionada con el manejo de pagos.

Esta clase está anotada con `@Service` y utiliza inyección de dependencias para interactuar con los repositorios necesarios.

Este cambio permite separar la lógica de negocio del controlador, mejorando la mantenibilidad y organización del código.

**Evidencia**

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final OfferingRepository offeringRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          EnrollmentRepository enrollmentRepository,
                          OfferingRepository offeringRepository) {
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.offeringRepository = offeringRepository;
    }
}---

# Prueba 5 — Uso de DTO en respuestas de pagos

Se implementó el objeto `PaymentResponseDto`, el cual define la estructura de las respuestas del módulo de pagos.

Este DTO evita que la API exponga directamente las entidades de la base de datos, permitiendo desacoplar la capa de persistencia de la capa de presentación.

Esto mejora la seguridad, la claridad de la API y la arquitectura general del sistema.

**Evidencia**

public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}---

# Conclusión

Las pruebas realizadas demuestran que los cambios implementados en la arquitectura del sistema se integran correctamente con la aplicación existente.

Las modificaciones realizadas mejoran la organización del código, separan responsabilidades entre capas y fortalecen la validación de datos, sin afectar el funcionamiento general del sistema.