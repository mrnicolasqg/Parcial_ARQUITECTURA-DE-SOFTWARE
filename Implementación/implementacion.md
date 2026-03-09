# Implementación de cambios arquitectónicos

## 1. Introducción

En el ADR del proyecto se propusieron diez mejoras arquitectónicas orientadas a mejorar la mantenibilidad, organización y escalabilidad del sistema ERP Iglesias.

Debido al alcance del proyecto, se seleccionaron cinco de estas decisiones arquitectónicas para ser implementadas en el código del sistema.

Estas implementaciones permiten mejorar la separación de responsabilidades, reducir el acoplamiento entre componentes y facilitar futuras extensiones del sistema.

---

# 2. Cambios implementados

A continuación se describen los cinco cambios que fueron implementados en el sistema.

---

# Cambio 1 — Introducción de capa Service

## Descripción

Se creó una capa de servicios para manejar la lógica de negocio del sistema.  
Anteriormente, parte de la lógica del sistema se encontraba directamente dentro de los controladores.

Con esta mejora se introdujeron clases de servicio que encapsulan la lógica del dominio.

## Archivos creados

PaymentService.java

## Beneficio

Este cambio permite:

- Separar la lógica de negocio de los controladores.
- Reducir la complejidad de los controllers.
- Facilitar el mantenimiento del código.

---

# Cambio 2 — Extracción de lógica desde PaymentController

## Descripción

Se identificó que el controlador encargado de los pagos contenía lógica de negocio relacionada con el procesamiento de pagos.

Esta lógica fue trasladada a la clase `PaymentService`, dejando al controlador únicamente responsable de manejar las solicitudes HTTP.

## Archivos modificados

PaymentController.java

## Archivos creados

PaymentService.java

## Beneficio

Este cambio mejora la arquitectura del sistema al separar las responsabilidades entre:

- Controladores (manejo de HTTP)
- Servicios (lógica de negocio)

---

# Cambio 3 — Implementación de DTOs

## Descripción

Se introdujeron objetos DTO (Data Transfer Objects) para representar las respuestas de la API en lugar de devolver directamente las entidades del modelo.

Esto permite desacoplar la estructura interna del modelo de datos de la interfaz pública de la API.

## Ejemplo de DTO

PaymentResponse

## Beneficio

- Mayor control sobre los datos expuestos.
- Mejor encapsulamiento del modelo de datos.
- Facilita futuras modificaciones en las entidades.

---

# Cambio 4 — Manejo centralizado de excepciones

## Descripción

Se implementó un manejador global de excepciones mediante la clase `GlobalExceptionHandler`.

Esta clase permite capturar excepciones del sistema y devolver respuestas HTTP uniformes.

## Archivo creado

GlobalExceptionHandler.java

## Beneficio

- Manejo consistente de errores.
- Mejores respuestas para el cliente.
- Reducción de código repetido en controladores.

---

# Cambio 5 — Validaciones con Bean Validation

## Descripción

Se agregaron validaciones en los modelos y en las solicitudes de la API utilizando anotaciones de Bean Validation.

Ejemplos de validaciones utilizadas:

- @NotNull
- @NotBlank
- @Email
- @Positive

## Beneficio

- Validación automática de datos de entrada.
- Reducción de errores en la API.
- Mejora de la calidad de los datos almacenados.

---

# 3. Conclusión

La implementación de estos cambios arquitectónicos permitió mejorar significativamente la organización del sistema ERP Iglesias.

Las mejoras introducidas contribuyen a:

- Mejor separación de responsabilidades
- Reducción del acoplamiento entre componentes
- Mayor claridad en la arquitectura del sistema

Estas modificaciones establecen una base más sólida para el crecimiento futuro del sistema y facilitan la incorporación de nuevas funcionalidades.