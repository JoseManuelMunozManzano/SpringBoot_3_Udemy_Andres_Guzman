# PROGRAMACION ORIENTADA A ASPECTOS (AOP)

Vamos a ver como trabajar con este paradigma de programación.

## Documentación

[Introducción AOP](./Documentacion/Introduccion-aop.pdf)

[Spring AOP](./Documentacion/ppt_spring_aop.pdf)

## Qué temas se tratan

- Controller anotado con @RestController
- @GetMapping
- ResponseEntity<?>
- Service anotado con @Service
- @Autowired
- Aspecto
  - Añadir dependencia al pom
  ```
  	<dependency>
  		<groupId>org.springframework.boot</groupId>
  		<artifactId>spring-boot-starter-aop</artifactId>
  	</dependency>
  ```
  - @EnableAspectJAutoProxy (en las últimas versiones no hace falta)
  - Clase anotada con @Component y @Aspect
  - @Before
  - @After
  - @AfterReturning
  - @AfterThrowing
  - JoinPoint
  - @Around
  - ProceedingJoinPoint
  - @Order
  - @Pointcut
- Logger

## Testing

Usando Postman, hacer el siguiente request usando un GET:

`http://localhost:8080/greeting`

`http://localhost:8080/greeting-error`
