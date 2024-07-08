# Spring Web API RESTful - CRUD

Veremos un ejemplo de app que implementa un CRUD MVC usando JPA.

## Qué temas se tratan

- Clase Entity
  - @Entity
  - @Table
  - @Id
  - @GeneratedValue(strategy = GenerationType.IDENTITY)
  - @NotBlank
  - @NotEmpty
  - @NotNull
  - @Size
  - @Min
- Interface Repository
  - CrudRepository
  - Método basado en nombre `existsBy`
- En Service, package-info.java
  - @NonNullApi
- Interface Service
  - Optional
- Clase Service que implementa la interface
  - @Service
  - @Transactional
  - @Nullable
  - Optional
    - ifPresent()
    - of()
    - orElseThrow()
- Clase Controller
  - @RestController
  - @RequestMapping
  - @GetMapping
  - @PathVariable
  - ReponseEntity
    - ok()
    - notFound()
    - build()
    - status()
    - body()
    - HttpStatus.CREATED
    - badRequest()
  - Optional
    - isPresent()
    - orElseThrow()
  - @PostMapping
  - @RequestBody
    - @Valid
  - @PutMapping
  - @DeleteMapping
  - BindingResult
    - hasFieldErrors()
    - Map<String, String>
    - getFieldErrors()
    - getField()
    - getDefaultMessage()
- VALIDACION CON PROPERTIES: Archivo de mensajes de validación personalizados en carpeta resources
  - Creación de archivo `messages.properties` para mantener mensajes de errores personalizados, en el idioma deseado, por ejemplo
  - Indicar en Entity Product, el message
    - @NotEmpty(message = "{NotEmpty.product.name}")
  - Clase de configuración (NOTA: No me ha parecido que hiciese falta)
    - En package principal creamos el fuente `AppConfig.java`
    - @Configuration
    - @PropertySource("classpath:messages.properties") - Esta es la configuración del fichero de mensajes personalizados
- VALIDACION PROGRAMATICA: Validación personalizada usando una clase de Validación
  - Creamos en el package principal la clase `ProductValidation.java`
  - @Component
  - implements Validator
    - Implementamos los métodos supports() y validate()
  - ValidationUtils
    - rejectIfEmptyOrWhitespace()
  - rejectValue()
  - Inyectamos el nombre de la clase `ProductValidation` en el controlador y lo usamos en los métodos create() y update()
- VALIDACION PERSONALIZADA USANDO ANOTACIONES: Creamos nuestra propia anotación de validación
  - Creamos el fuente `IsRequired.java` en el package `validation`
  - Anotamos con `@Retention(RetentionPolicy.RUNTIME)` indicando que se ejecuta en tiempo de ejecución
  - Anotamos con `@Target({ElementType.FIELD, ElementType.METHOD})` para indicar donde podemos usar esta anotación
  - Indicamos que es una anotación usando `@interface`
  - Dada una anotación existente, por ejemplo, @NotBlank, vamos a su código y copiamos para llevar a nuestra anotación personalizada lo siguiente:
    - ```
      String message() default "{jakarta.validation.constraints.NotBlank.message}";
      Class<?>[] groups() default { };
      Class<? extends Payload>[] payload() default { };
      ```
  - Cambiamos el message: `String message() default "es requerido usando anotación personalizada.";`
  - Creamos una clase para usar la anotación, `RequiredValidation.java` que debe implementar `ConstraintValidator<IsRequired, String>`, e implementamos el método `isValid()`
  - Volvemos a la anotación `IsRequired` y anotamos con la anotación `@Constraint(validatedBy = RequiredValidation.class)`
  - Vamos a nuestro entity `Product` y en el atributo `description` vamos a usar nuestra anotación personalizada `@IsRequired`
  - Podemos incluso usar `messages.properties` para cambiar nuestro texto a otro personalizado.
- VALIDACION PERSONALIZADA USANDO ANOTACIONES Y VALIDANDO CONTRA LA BD: Por ejemplo, validamos contra la BD que el campo exista
  - Creamos el fuente `IsExistsBD.java` en el package `validation` tal y como hicimos con IsRequired.java
  - Creamos la clase de validación `IsExistsBDValidation.java` que da soporte a esta anotación, igual que hicimos con RequiredValidation.java
    - Anotamos con @Component porque nos hace falta inyectar ProductService
  - Volvemos a la anotación `IsExistsBD` y anotamos con la anotación `@Constraint(validatedBy = IsExistsBDValidation.class)`
  - Vamos a nuestro entity `Product` y en el atributo `sku` vamos a usar nuestra anotación personalizada `@IsExistsBD`
  - En application.properties:
  - ```
    # Para evitar la doble validación javax y Spring en IsExistsBDValidation.java
    spring.jpa.properties.javax.persistence.validation.mode=none
    ```

Para las Validaciones (@Valid) se añade al POM

```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## Testing

Correr la siguiente imagen de mysql:

```
  docker container run \
  -e MYSQL_USER=springstudent \
  -e MYSQL_PASSWORD=springstudent \
  -e MYSQL_ROOT_PASSWORD=sasa1234 \
  -e MYSQL_DATABASE=db_springboot \
  -dp 3306:3306 \
  --name db_springboot \
  --volume db_springboot:/var/lib/mysql \
  mysql:8.0
```

En Squirrel crear el siguiente alias:

```
  Name: MySql_Docker
  Driver: MySQL Driver
  URL: jdbc:mysql://
  User Name: springstudent
  Password: springstudent

  Pulsar properties, y en la ventana que se abre, pulsar Driver properties.
  Hacer check en Use driver properties
  Hacer check en host e informar localhost
  Hacer check en port e informar 3306
  Hacer check en dbname e informar db_springboot
```

Creamos la tabla de forma manual:

```
CREATE TABLE products (
 id BIGINT NOT NULL AUTO_INCREMENT,
 name VARCHAR(45) NOT NULL,
 price INT NOT NULL,
 description TEXT NOT NULL,
 sku VARCHAR(45) NULL,
 PRIMARY KEY (id)
);
```

Para las pruebas en Postman, ir al archivo [Postman](./Postman/apiRestfulCrud.postman_collection.json)

En Postman probar las siguientes rutas:

- FindAll (GET): http://localhost:8080/api/products
- FindOne (GET): http://localhost:8080/api/products/{id}
- Save (POST): http://localhost:8080/api/products/
- Update (PUT): http://localhost:8080/api/products/{id}
- Delete (DELETE): http://localhost:8080/api/products/{id}

Para el POST, indicar en Postman, de uno en uno, los siguientes productos:

```
{
    "name": "Producto limpieza",
    "price": 5000,
    "description": "Alguna descripción para el producto de limpieza",
    "sku": 1234
}


{
    "name": "Producto tecnología",
    "price": 7000,
    "description": "Alguna descripción para el producto de tecnología",
    "sku": 5678
}
```

Y en BD, en Squirrel: `select * from products;`

Para el PUT, indicar en Postman la ruta `http://localhost:8080/api/products/2`

```
{
    "name": "Producto tecnología TV",
    "price": 10000,
    "description": "Alguna descripción para el producto de tecnología TV"
}
```

Y en BD, en Squirrel: `select * from products;`

Para el DELETE, indicar en Postman la ruta: `http://localhost:8080/api/products/2`

Y en BD, en Squirrel: `select * from products;`

Para probar las validaciones, en Postman, en el Post, indicar en el body el siguiente JSON:

```
{
    "price": 200
}
```

Aparecerá la siguiente respuesta:

```
{
    "price": "El campo price debe ser un valor numérico mayor o igual que 500!",
    "name": "El campo name es requerido!",
    "description": "El campo description es requerido, por favor!"
}
```

Para probar la validación de existencia de valor de campo en BD en Postman, en el Post, indicar en el body el siguiente JSON:

```
{
    "sku": 1234
}
```

La respuesta es (fijarse en sku):

```
{
    "price": "El campo price no puede ser nulo, KO!",
    "name": "El campo name es requerido!",
    "description": "El campo description es requerido, mensaje en properties",
    "sku": "El campo sku ya existe en la base de datos!"
}
```

Si indicamos un valor de sku como 1, como no existe, no saldrá nada.
