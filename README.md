# SPRING FRAMEWORK 6 & SPRING BOOT 3 DESDE CERO A EXPERTO

Del curso UDEMY `https://www.udemy.com/course/spring-framework-5/` del profesor Andrés Guzmán.

Se van a dar los siguientes temas:

- Inyección de Dependencia
- Modelo Vista y Controlador MVC
- API RESTFul
- Programación Reactiva WebFlux
- Programación Orientada a Aspectos AOP
- JDK Mínimo 17
- Cero Configuración
- Spring Security JWT
- Spring Data JPA, Validación, Seguridad, JUnit5, Microservicios, Cloud
- Deploy en AWS EC2
- Angular y React
- Thymeleaf
- Exportar PDF y XLS
- Programación Funcional y Reactiva con Reactor
- Spring Data Mongo
- Subida de Archivos
- Bootstrap

¿Qué es Spring Boot?

- Es rápido, seguro para app conectada a cualquier tipo de motor
- Crea aplicaciones con Spring Framework, RESTFul, MVC, IoC, Data Access
- Incluye un servidor embebido Tomcat, optimizado para aplicaciones Spring Boot
- Versiones de dependencia son auto-administradas
- Auto-configuración de clases proveídas por defecto (Hibernate, JPA)
- Herramientas de desarrollo devtools (LiveReload)
- Monitoreo y métricas para producción, auditing, health (Spring Actuator)
- Listo para producción, llegar y ejecutar, jar ejecutables (incluye Maven y no requiere war)
- Spring Data JPA, Mongo, Seguridad, Testing JUnit, Microservicios, Cloud, Reactivo

## Relaciones entre entities

Donde vaya el mappedBy es la clase principal o padre.

Donde va el @JoinColumn es la clase hija o dependiente, pero que es la dueña de la relación, donde aparecerá el campo nuevo.

Documentación: `https://hibernate.org/orm/documentation/6.4/`, `https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html`, `https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#associations`

## Proyectos

Ver los README de los distintos proyectos:

- 01-springboot-web
  - Genérico sobre dintintas funcionalidades del core de Spring Boot
- 02-springboot-di
  - Sin inyección de dependencias. Como se haría la inyección de dependencias manual
- 02-springboot-di2
  - Con inyección de dependencias
- 03-springboot-difactura
  - Un proyecto ejemplo donde se usa la inyección de dependencias
  - Se ven algunas anotaciones nuevas (@PostConstruct, @PreDestroy y @JsonIgnoreProperties)
- 04-springboot-error
  - Manejo de excepciones
- 05-springboot-interceptor
  - Explicación de interceptores
- 06-springboot-horario
  - Ejemplo usando interceptores
- 07-springboot-aop
  - Aspectos
- 08-springboot-jpa
  - JPA e Hibernate
- 09-springboot-jpql
  - Continuación de JPA, pero en otro proyecto para separarlo
- 10-springboot-jpa-ciclo-vida
  - Vemos eventos del ciclo de vida del objeto entity de persistencia
- 11-springboot-relationships
  - Relaciones entre las clases Entity usando JPA
- 12-springboot-relationships_2
  - Es un ejemplo de relación @OneToMany donde, en vez de crear una tabla intermedia, añadimos el campo de foreign key a una de las tablas
- 13-springboot-relationships_3
  - Es un ejemplo de relación @OneToMany donde, en vez de crear una tabla intermedia de forma automática, indicamos una tabla intermedia ya existente
- 14-springboot-relationships_onetomany_bidireccional
- 15-springboot-relationships-one-to-one
- 16-springboot-relationships-one-to-one-bidireccional
- 17-springboot-relationships-many-to-many
- 18-springboot-relationships-many-to-many-bidireccional
- 19-springboot-crud
  - Implementa un CRUD con API Rest, con JPA y BBDD
  - Se ven distintas formas de hacer validaciones
- 20-springboot-crud-jwt
  - Se parte del proyecto anterior `19-springboot-crud` y se añade todo el tema de JSON Web Token
- 21-springboot-crud-jwt-bidireccional
  - Se parte del proyecto anterior `20-springboot-crud` y se añade bidireccionalidad entre los entities User y Role, para ver el problema de error Bucle
- 22-oauth
  - Ejemplo de como implementar OAuth2 V2.1 para poder tener nuestro servidor de autorización en nuestros proyectos y poder generar un token JWT
  - Consta de los siguientes proyectos:
    - 22-springboot-security-auth-server
      - Nuestro servidor de aplicaciones
    - 22-springboot-security-client
      - Cliente backend del servidor de aplicaciones
