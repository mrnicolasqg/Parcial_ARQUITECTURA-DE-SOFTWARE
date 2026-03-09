# ADR-001 — Mejora de la arquitectura del sistema ERP Iglesias

## Estado
Propuesto / Parcialmente Implementado

---

# 1. Contexto

El proyecto analizado corresponde a un sistema ERP para la gestión de iglesias.  
El sistema permite administrar iglesias, personas, cursos, inscripciones, ofrendas y pagos.

El proyecto utiliza el siguiente stack tecnológico.

## Backend
- Java
- Spring Boot
- JPA / Hibernate

## Base de datos
- PostgreSQL

## Infraestructura
- Docker
- Docker Compose

## Frontend
- Aplicación web ubicada en la carpeta `frontend`.

Durante el análisis del sistema se identificaron algunos problemas arquitectónicos que afectan la mantenibilidad y escalabilidad del proyecto.

Problemas detectados:

- Algunos controladores contienen lógica de negocio.
- No existe una separación clara entre capas.
- Algunas entidades se devuelven directamente en las respuestas HTTP.
- El manejo de errores no está centralizado.
- Falta modularidad en la organización del código.

Estos aspectos afectan la mantenibilidad del sistema y pueden generar problemas a medida que el proyecto crezca o se agreguen nuevas funcionalidades.

---

# 2. Decisiones arquitectónicas propuestas (10 cambios)

A continuación se presentan diez decisiones arquitectónicas orientadas a mejorar la calidad del sistema mediante el uso de principios SOLID y patrones de diseño.

---

## ADR-01 — Introducción de capa Service

Principio aplicado:  
**Single Responsibility Principle (SRP)**

Se propone introducir una capa `Service` que contenga la lógica de negocio del sistema, separándola de los controladores.

Beneficios:

- Mejor separación de responsabilidades.
- Controladores más simples.
- Código más mantenible.

---

## ADR-02 — Reorganización de paquetes por dominio

Se propone reorganizar el proyecto separando el código por módulos funcionales.

Ejemplo de organización:

- church
- person
- course
- payment
- offering
- enrollment
- user

Beneficios:

- Mayor modularidad.
- Código más organizado.
- Mejor escalabilidad.

---

## ADR-03 — Extracción de lógica desde Controllers hacia Services

Se identificó que algunos controladores contienen lógica de negocio.

Se propone mover esa lógica hacia clases de servicio.

Beneficios:

- Controladores más simples.
- Mejor organización del código.

---

## ADR-04 — Implementación de Global Exception Handler

Patrón aplicado:  
**Centralized Error Handling**

Se propone implementar una clase `GlobalExceptionHandler` que capture las excepciones del sistema y devuelva respuestas HTTP uniformes.

Beneficios:

- Manejo consistente de errores.
- Mejores respuestas para el cliente.

---

## ADR-05 — Uso de DTOs

Principio aplicado:  
**Encapsulamiento**

Se propone utilizar DTOs para las respuestas y solicitudes de la API en lugar de devolver entidades directamente.

Ejemplo:

- PaymentRequest
- PaymentResponse

Beneficios:

- Mayor control sobre los datos expuestos.
- Desacoplamiento entre API y modelo de datos.

---

## ADR-06 — Uso de validaciones con Bean Validation

Se propone utilizar anotaciones de validación como:

- `@NotNull`
- `@NotBlank`
- `@Email`
- `@Positive`

Beneficios:

- Validación automática de datos de entrada.
- Reducción de errores.

---

## ADR-07 — Relaciones JPA explícitas

Se propone mejorar las entidades utilizando anotaciones de relación de JPA:

- `@ManyToOne`
- `@OneToMany`
- `@JoinColumn`

Beneficios:

- Modelo de datos más claro.
- Mejor manejo de relaciones entre entidades.

---

## ADR-08 — Introducción de patrón Factory para pagos

Patrón aplicado:  
**Factory Pattern**

Dependiendo del tipo de pago se puede crear un objeto especializado.

Ejemplo:

- EnrollmentPayment
- OfferingPayment

Beneficios:

- Código más extensible.
- Facilita agregar nuevos tipos de pago.

---

## ADR-09 — Implementación de logging estructurado

Se propone agregar registros de eventos importantes del sistema utilizando herramientas de logging.

Ejemplo:

LoggerFactory

Beneficios:

- Mejor monitoreo del sistema.
- Facilita la depuración de errores.

---

## ADR-10 — Mejora de nombres y claridad del código

Se propone refactorizar nombres de métodos y variables para mejorar la legibilidad del código.

Beneficios:

- Código más comprensible.
- Mejor mantenimiento.

---

# 3. Cambios implementados (5 de los 10)

De los cambios propuestos se implementaron los siguientes cinco:

### Cambio 1 — Introducción de capa Service

Se creó una capa de servicios para manejar la lógica de negocio del sistema.

---

### Cambio 2 — Extracción de lógica desde PaymentController

La lógica relacionada con operaciones de pago fue movida desde `PaymentController` hacia una clase `PaymentService`.

---

### Cambio 3 — Implementación de DTOs

Se implementaron DTOs para manejar las respuestas de la API.

Ejemplo:

- PaymentResponse

---

### Cambio 4 — Implementación de validaciones

Se agregaron validaciones utilizando anotaciones de Bean Validation para verificar los datos de entrada.

---

### Cambio 5 — Mejora en la estructura del código

Se reorganizaron algunas clases para mejorar la separación de responsabilidades.

---

# 4. Consecuencias

La aplicación de estas decisiones arquitectónicas genera los siguientes efectos positivos:

- Mejor separación de responsabilidades.
- Reducción del acoplamiento entre componentes.
- Mayor claridad en la arquitectura del sistema.
- Mayor facilidad para agregar nuevas funcionalidades.

Posibles trade-offs:

- Se incrementa ligeramente la cantidad de clases del sistema.
- Requiere mayor disciplina en la organización del código.

A pesar de ello, los beneficios en mantenibilidad y escalabilidad justifican la aplicación de estas mejoras arquitectónicas.