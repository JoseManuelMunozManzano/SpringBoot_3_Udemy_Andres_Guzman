# MANEJO DE EXCEPCIONES

Se va a realizar un proyecto de ejemplo para aprender como se manejan las excepciones usando SpringBoot

## Creando clase DTO de Error

Vamos a crear un controlador especial que se encargará de capturar las excepciones y que va a estar mapeado a una excepción de error.

El error se va a devolver como un JSON personalizado, mucho más amistoso del que se devuelve por defecto cuando ocurre una excepción no controlada.

Tenemos dos opciones para devolver este JSON:

- Usar un HashMap
- Crear una clase DTO con atributos. Esto es lo que vamos a hacer

Creamos el package models, y dentro la clase Error.java

También creamos, en el package controllers, el controlador HandlerExceptionController.java, anotándolo con @RestControllerAdvice

Esta anotación indica que el controlador, en vez de estar mapeado a una ruta URL, va a estar mapeado a un lanzamiento de excepción.

Creamos un método que anotamos con @ExceptionHandler e indicamos la o las excepciones que queremos mapear. Para una excepción no hace falta indicar llaves.

El método devuelve un ResponseEntity. Esto es una entidad que va en el cuerpo de la respuesta. Es un genérico donde indicamos nuestro tipo, como Error, o ? para indicar cualquier tipo.

El argumento de este método es del tipo Exception, la excepción que capturamos.

```
  // Usando ?

  @ExceptionHandler({ArithmeticException.class})
  public ResponseEntity<?> divisionByZero(Exception ex) {

    return ResponseEntity.internalServerError().body("error 500");
  }

  // Usando nuestro DTO

  @ExceptionHandler({ArithmeticException.class})
  public ResponseEntity<Error> divisionByZero(Exception ex) {

    Error error = new Error();
    error.setDate(new Date());
    error.setError("Error división por cero");
    error.setMessage(ex.getMessage());
    error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

    // return ResponseEntity.internalServerError().body(error);
    // Otra forma
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
  }
```

## Personalizando error 404

Vamos a personalizar el típico error 404 (Page not found) que por defecto maneja Spring.

Creamos el método notFoundEx() en la clase controller HandlerExceptionController.java

```
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Error> notFoundEx(NoHandlerFoundException e) {

    Error error = new Error();
    error.setDate(new Date());
    error.setError("Api REST no encontrada");
    error.setMessage(e.getMessage());
    error.setStatus(HttpStatus.NOT_FOUND.value());

    // Si no queremos personalizar el mensaje de error puedo indicar:
    // return ResponseEntity.notFound().build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
  }
```

Este código por si solo no funciona, porque Spring da su propio formato que sobreescribe este.

Tenemos que indicar la siguiente propiedad en el fichero application.properties para que funcione nuestro código:

`spring.web.resources.add-mappings=false`

## Personalizando página de error para la excepción NumberFormatException

Hemos creado en el controller AppController.java el método index2() que dará como resultado una excepción NumberFormatException que vamos a manejar de forma personalizada.

Podríamos añadir esta excepción al controlador HandlerExceptionController.java, a la anotación @ExceptionHandler siguiente: `@ExceptionHandler({ArithmeticException.class, NumberFormatException.class})`, pero lo vamos a hacer diferente, devolviendo un Map en vez de un ResponseEntity.

Notar que, al ser un map, para indicar el status devuelto usamos la anotación @ResponseStatus.

```
  @ExceptionHandler(NumberFormatException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> numberFormatException(Exception ex) {

    Map<String, String> error = new HashMap<>();
    error.put("date", new Date().toString());
    error.put("error", "Número inválido o incorrecto, no tiene formato de dígito!");
    error.put("message", ex.getMessage());
    error.put("status", Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    return error;
  }
```

## Manejando excepción personalizada creando clases Model y Service

Vamos a crear nuestra propia excepción y la vamos a manejar de dos formas distintas.

- Forma antigua
- Usando API stream y Optionals (programación funcional)

Dentro del package models, creamos el package domain y dentro creamos las clases POJO User.java y Role.java

Creamos un package services para que trabaje con la lógica de negocio (los datos), evitando tener que acoplarlos directamente al controlador, con la idea de trabajar por capas.

Dentro creamos la interface UserService.java y su implementación UserServiceImpl.java, anotándola con @Service

### Añadiendo método handler y lanzando excepción Null Pointer

Inyectamos el service en el controlador AppController.java y creamos un nuevo método handler llamado show() que devuelve un JSON a partir de la clase User.java

### Creando clase Exception personalizada

Vamos a ver como manejar las excepciones NullPointerException y HttpMessageNotWritableException, porque después lo podemos traducir a nuestra propia forma de excepción.

Creamos un método userNotFoundException() que maneja tanto NullPointerException como HttpMessageNotWritableException en el controller HandlerExceptionController.java

```
  @ExceptionHandler({NullPointerException.class, HttpMessageNotWritableException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> userNotFoundException(Exception ex) {

    Map<String, String> error = new HashMap<>();
    error.put("date", new Date().toString());
    error.put("error", "El usuario o role no existe!");
    error.put("message", ex.getMessage());
    error.put("status", Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    return error;
  }
```

Creamos un package exceptions y dentro creamos la clase UserNotFoundException.java que hereda de RuntimeException, y cambiamos el mensaje de la excepción.

En el controller AppController, si el user devuelto es null lanzamos esta excepción.

```
    // Forma antigua

    if (user == null) {
      throw new UserNotFoundException("Error. El usuario no existe!");
    }
```

NOTA: Hay programadores que prefieren manejar las excepciones en el controller y no en el service, y al revés.

Y en el controller HandlerExceptionController.java modificamos nuestro @ExceptionHandler para tratar esta excepción: `@ExceptionHandler({NullPointerException.class, HttpMessageNotWritableException.class, UserNotFoundException.class})`

### Lanzamiento de excepción usando Api Optional de Java 8

Es una alternativa a la forma de lanzar las excepciones a la manera tradicional vista arriba.

Se añade un método findByIdOptional() en UserService que devuelve un Optional de User.

Si el user está presente, lo devolvemos y si no está presente vamos a devolver un empty.

Se crea en el controller AppController.java un nuevo método show2 para tratar este Optional. Usamos el método findByIdOptional() y si el user no está presente usamos el método orElseThrow()

`User user = service.findByIdOptional(id).orElseThrow(() -> new UserNotFoundException("Error. El usuario no existe!"));`

## Optimizando código con el Api Stream de Java 8

Vamos a crear una clase de configuración de Spring, AppConfig.java, que devuelve una lista de User que luego inyectaremos en UserServiceImpl.java

También vamos a hacer un método en UserServiceImpl.java, llamado findByIdFunctional, para mostrar una forma funcional, usando streams, para encontrar un User por id.

`return users.stream().filter(u -> u.getId().equals(id)).findFirst();`

## Qué temas se tratan

- Controlador anotado con @RestController
- @GetMapping
- Clase DTO de error en package models
- Controlador que se encarga de capturar las excepciones, anotado con @RestControllerAdvice
- Método anotado con @ExceptionHandler
- ResponseEntity
- Map
- HttpStatus
- @ResponseStatus
- Propiedad `spring.web.resources.add-mappings=false`
- Creación de excepciones personalizadas
- Creación de service, interface e implementación anotada con @Service
- @Autowired
- @PathVariable
- Optional
  - ofNullable()
  - orElseThrow()
  - isEmpty()
- Clase de configuración anotada con @Configuration
- @Bean
- Programación funcional
  - stream()
  - filter()
  - findFirst()
- Uso de Postman

## Testing en Postman

- Creando clase DTO de Error: `http:\\localhost:8080\app`
- Error 404: `http:\\localhost:8080\no_existe`
- Personalizando página de error para la excepción NumberFormatException: `http:\\localhost:8080\app2`
- Añadiendo método handler y lanzando excepción HttpMessageNotWritableException: `http:\\localhost:8080\show\1`
- Añadiendo método handler y lanzando excepción NullPointerException: `http:\\localhost:8080\show\1000`
- Lanzamiento de excepción usando Api Optional de Java 8: `http:\\localhost:8080\show2\1000`
