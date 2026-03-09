# Diagnóstico del Stack Tecnológico

## 1. Introducción

El presente documento describe el análisis del stack tecnológico y la estructura del sistema ERP Iglesias.  
El objetivo de este diagnóstico es identificar las tecnologías utilizadas, comprender la organización del proyecto y detectar posibles problemas arquitectónicos que puedan afectar la mantenibilidad, escalabilidad y claridad del sistema.

Este diagnóstico sirve como base para la propuesta de mejoras arquitectónicas planteadas posteriormente en el ADR.

---

# 2. Stack tecnológico

El sistema ERP Iglesias está construido utilizando las siguientes tecnologías.

### Backend
- Java  
- Spring Boot  
- Spring Data JPA  
- Hibernate  

El backend es responsable de manejar la lógica del sistema, exponer los endpoints de la API y gestionar la comunicación con la base de datos.

### Base de datos
- PostgreSQL  

La base de datos almacena la información principal del sistema, incluyendo iglesias, personas, cursos, pagos, inscripciones y ofrendas.

Las entidades principales identificadas en el modelo de datos son:

- Church  
- Person  
- Course  
- Enrollment  
- Offering  
- Payment  
- App_User  

### Infraestructura
- Docker  
- Docker Compose  

Docker se utiliza para ejecutar los servicios del sistema en contenedores, lo que facilita la configuración del entorno de desarrollo y la ejecución local del proyecto.

El archivo docker-compose.yml permite levantar los servicios necesarios como la base de datos y el backend.

### Frontend
El proyecto incluye una aplicación web ubicada en la carpeta frontend.

El frontend se encarga de la interacción con el usuario y consume los servicios REST proporcionados por el backend.

---

# 3. Estructura del proyecto

El proyecto presenta una estructura dividida en dos componentes principales.

erp-iglesias-main  
│  
├── backend  
│   └── src/main/java/com/iglesia  
│  
├── frontend  
│  
├── docker-compose.yml  

### Backend

Dentro del backend se encuentran los componentes principales de la API:

- Controllers  
- Entities  
- Repositories  
- Configuración de Spring Boot  

Los controladores se encargan de recibir las solicitudes HTTP provenientes del frontend y procesarlas.

---

# 4. Funcionamiento general del sistema

El funcionamiento general del sistema sigue el siguiente flujo:

1. El usuario interactúa con el frontend.  
2. El frontend envía solicitudes HTTP al backend.  
3. Los controladores del backend procesan la solicitud.  
4. El backend interactúa con la base de datos PostgreSQL mediante JPA.  
5. Los datos se devuelven al frontend en formato JSON.

Este modelo sigue una arquitectura típica de aplicaciones web basadas en API REST.

---

# 5. Problemas detectados

Durante el análisis del sistema se identificaron algunos aspectos que pueden mejorarse desde el punto de vista arquitectónico.

### Lógica de negocio en controladores

Algunos controladores contienen lógica de negocio que debería estar ubicada en una capa de servicios.

Esto genera controladores con demasiada responsabilidad y dificulta el mantenimiento del código.

### Falta de separación clara de capas

El proyecto no presenta una separación clara entre las capas de:

- Controladores  
- Lógica de negocio  
- Acceso a datos  

Esto puede provocar alto acoplamiento entre componentes.

### Falta de manejo centralizado de errores

Las excepciones no se manejan de forma centralizada dentro de la aplicación.

Esto puede generar respuestas inconsistentes en la API y dificultar el manejo de errores.

### Uso directo de entidades en las respuestas

Algunas entidades del modelo de datos se devuelven directamente en las respuestas HTTP.

Esto genera un acoplamiento entre la API y la estructura interna del modelo de datos.

### Organización limitada del código por dominio

El proyecto no presenta una organización clara por módulos de dominio, lo que puede dificultar la escalabilidad del sistema a medida que se agregan nuevas funcionalidades.

---

# 6. Conclusión del diagnóstico

El sistema ERP Iglesias presenta una arquitectura funcional y permite gestionar las principales operaciones del sistema.

Sin embargo, durante el análisis se identificaron varias oportunidades de mejora relacionadas con:

- Separación de responsabilidades  
- Organización del código  
- Manejo de errores  
- Encapsulamiento de datos  
- Modularización del sistema  

Estas observaciones justifican la propuesta de mejoras arquitectónicas presentadas en el ADR del proyecto.