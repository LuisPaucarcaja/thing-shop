# Thing Shop 🛒

## Descripción
**Thing Shop** es una aplicación de **e-commerce** construida bajo una **arquitectura de microservicios** utilizando **Java y Spring Boot**. El sistema implementa comunicación **síncrona (REST)** y **asíncrona (eventos con RabbitMQ)** para desacoplar servicios y garantizar escalabilidad.

El proyecto está orientado a prácticas de **backend moderno**, aplicando principios de diseño, separación de responsabilidades y buenas prácticas de desarrollo.

---

## Arquitectura General
- **API Gateway** para centralizar el acceso a los servicios
- **Servicios independientes** por dominio
- **Autenticación basada en JWT**
- **Mensajería asíncrona con RabbitMQ** para eventos
- **Persistencia con JPA/Hibernate**

### Comunicación entre servicios
- **Síncrona:** APIs REST entre microservicios
- **Asíncrona:** Eventos publicados y consumidos mediante **RabbitMQ**

---

## Microservicios

### 🔐 Auth Core
Responsable de la **autenticación y autorización** del sistema.
- Validación de tokens **JWT**
- Filtros de seguridad
- Manejo de usuarios autenticados

**Tecnologías:** Spring Boot, JWT, Spring Security

---

### 🚪 Gateway Service
Funciona como **puerta de entrada** a los microservicios.
- Configuración de CORS
- Enrutamiento de solicitudes

**Tecnologías:** Spring Cloud Gateway

---

### 📦 Inventory Service
Gestiona el **inventario de productos**.
- Control de stock
- Reservas de productos
- Validación de disponibilidad
- Comunicación con Order Service

Incluye:
- CRUD de inventario
- Manejo de reservas
- **Consumo y publicación de eventos con RabbitMQ**

**Tecnologías:** Spring Boot, JPA, RabbitMQ, Feign Client

---

### 📧 Notification Service
Encargado del **envío de notificaciones**.
- Envío de correos electrónicos
- Consumo de eventos asíncronos

Ejemplo:
- Verificación de correo mediante eventos

**Tecnologías:** Spring Boot, RabbitMQ, Email Services

---

### 🧾 Order Service
Gestiona las **órdenes de compra**.
- Creación y validación de órdenes
- Manejo de pagos
- Integración con inventario

Incluye:
- Control de carrito
- Validación de stock
- Manejo de excepciones

**Tecnologías:** Spring Boot, JPA, REST, RabbitMQ

---

## Tecnologías Utilizadas
- **Java 17+**
- **Spring Boot**
- **Spring Data JPA / Hibernate**
- **Spring Security + JWT**
- **RabbitMQ**
- **Swagger / OpenAPI**
- **Maven**
- **MySQL / PostgreSQL (configurable)**

---

## Ejecución del Proyecto

### Requisitos
- Java 17+
- Maven
- RabbitMQ
- Base de datos (MySQL o PostgreSQL)

### Pasos generales
```bash
# Clonar repositorio
git clone https://github.com/LuisPaucarcaja/thing-shop.git

# Ejecutar cada microservicio
mvn spring-boot:run
```

> ⚠️ Cada microservicio cuenta con su propio `application.yml` para configuración independiente.

---

## Buenas Prácticas Aplicadas
- Separación por capas (Controller, Service, Repository)
- Manejo global de excepciones
- DTOs y mappers
- Arquitectura orientada a eventos
- Versionamiento con Git

---

## Autor
**Luis Paucarcaja**  
📌 Backend Developer Junior  
🔗 GitHub: https://github.com/LuisPaucarcaja

---

## Estado del Proyecto
📈 En desarrollo / proyecto personal orientado a aprendizaje y portafolio.

