# SPRING WEB

Características que se ven en esta app:

## @SpringBootApplication

- Incluye autoconfiguración de las dependencias
- Incluye, entre otras, la anotación @ComponentScan, que escanea componentes y los registra como componentes de Spring. Esto permite hacer uso de la inyección de dependencias
- Un componente es un objeto cuyo ciclo de vida y contexto es manejado por el framework Spring
- Un componente es lo mismo que un bean
- Todas las clases/packages del proyecto deberán estar dentro del package principal, que es donde se encuentra el fuente SpringbootWebApplication.java

## Creación de controlador

- Crear un package `controllers`, dentro del package principal
- Crear siempre el nombre de la clase indicando el sufijo `Controller`
  - Ejemplo `UserController.java`
- Un controlador es una clase común y corriente que maneja métodos handler, es decir maneja peticiones de usuario (request) y devuelve respuestas al usuario (response) en formato HTML, JSON, XML...
- Para indicar que una clase es un controlador de Spring, se utiliza la anotación @Controller, que es un estereotipo, una especialización de un @Component.
- Usando @Controller indicamos que vamos a trabajar con vistas HTML del lado del servidor, usando plantillas Thymeleaf o JSP. Por tanto, devolveremos el nombre de esa plantilla como un String. No se indica la extensión
  - Ejemplo `return "details";`

### Request

Un request tiene distintos tipos o métodos que se pueden usar.

- En el ejemplo usamos @GetMapping, que sirve para peticiones a través de rutas URL que se colocan en el navegador, o enlaces en una web
  - Ejemplo de endpoint `@GetMapping("/")`
- Un método POST (@PostMapping) se usa para enviar data nueva, usando un formulario, que se suele enviar en formato JSON
- Un método PUT (@PutMapping) se usa para modificar data existente, usando un formulario, que se suele enviar en formato JSON
- Un método DELETE (@DeleteMapping) se usa para eliminar data existente, usando un formulario, que se suele enviar en formato JSON

### Templates

Carpeta resources/templates

- En esa carpeta crearemos nuestras plantillas Thymeleaf (o JSP)
- Se crean como un file con extensión .html
  - Ejemplo `details.html`

### Pasar datos a una vista usando Model

Ver método `details2` en la clase UserController.

- Se utiliza el patrón de inyección de dependencias para pasar datos a una vista, usando Model (que se importa de springframework.ui)
- Se utiliza el método addAttribute(nombre_atributo, valor_atributo) para pasar la información a la vista

```
  @GetMapping("/details")
  public String details2(Model model) {

    model.addAttribute("title", "Hola Mundo Spring Boot");
    model.addAttribute("name", "José Manuel");
    model.addAttribute("lastname", "Muñoz Manzano");

    return "details2";
  }
```

Para la parte de la vista, se usa el template Thymeleaf `details2.html`

- Hay que configurar el xmlns (espacio de nombres XML) para usar Thymeleaf, dándole un alias (th)
  - `<html lang="en" xmlns:th="http://www.thymeleaf.org">`
- Usaremos el alias del xmlns (th) seguido de dos puntos y text para obtener texto pasado por el método del controlador. Este texto será una variable que se indica ${variable}
  - `<title th:text="${title}">Document</title>`
  - El texto Document no se renderiza y se podría eliminar

### Pasar datos a una vista usando Map

Ver método `details3` en la clase UserController.

- Se usa `Map<String, Object>` donde el primer tipo siempre debe ser String (corresponde al nombre del atributo) y el segundo dependerá de qué datos queremos pasar (corresponde al valor del atributo)
- Utilizaremos el método put(key, value) para pasar la información a la vista
- Recomiendo usar Model

```
  @GetMapping("/details2")
  public String details3(Map<String, Object> model) {

    model.put("title", "Hola Mundo Spring Boot");
    model.put("name", "José Manuel");
    model.put("lastname", "Muñoz Manzano");

    return "details2";
  }
```

Para la parte de la vista, se vuelve a usar el template Thymeleaf `details2.html`

### Pasar datos a una vista usando ModelMap

- Es una implementación de un Map para construir y pasar datos a una vista
- Simplemente otra forma de hacer lo mismo que con Model o Map

```
  @GetMapping("/list")
  public String list(ModelMap model) {

    List<User> users = new ArrayList<>();

    model.addAttribute("users", users);
    return "list";
  }
```

## Rest Controller

- Ver fuente `UserRestController.java`
- Si queremos mostrar la información en formato JSON en vez de en una vista
- Así podemos compartir información con aplicaciones cliente
- Usamos la anotación `@RestController`, que combina la anotación @Controller y @ResponseBody, que es el cuerpo de la respuesta
- @ResponseBody por defecto devuelve el contenido como un JSON gracias a una API que se llama Jackson
- Para montar este objeto JSON se puede usar un objeto `Map<atributo, valor>`

```
  @GetMapping("/details")
  public Map<String, Object> details() {

    Map<String, Object> body = new HashMap<>();
    body.put("title", "Hola Mundo Spring Boot");
    body.put("name", "José Manuel");
    body.put("lastname", "Muñoz Manzano");

    return body;
  }
```

### Anotación @RequestMapping

- Con esta anotación a nivel de clase podemos indicar una ruta base a nuestro controlador

```
  @RestController
  @RequestMapping("/api")
  public class UserRestController {
  }
```

- En vez de @GetMapping() también podemos usar @RequestMapping() en el método y funciona igual, porque por defecto un @RequestMapping es un método GET

```
  @RequestMapping("/details")
  public Map<String, Object> details() {
  }
```

- Se puede indicar el tipo de método a realizar. En ese caso también tenemos que indicar la url como valor del atributo path

```
  @RequestMapping(path = "/details", method = RequestMethod.GET)
  public Map<String, Object> details() {
  }
```

## Rest Controller usando anotación @Controller y @ResponseBody separadamente

- Ver fuente `UserRestController2.java`
- Se puede usar la anotación `@Controller`, en vez de la recomendada @RestController
- No olvidar indicar la anotación @ResponseBody en los métodos para devolver un JSON
- Usando @Controller nos sirve para ser más flexible, pudiendo tener métodos que nos devuelvan JSON y otros que nos devuelva una vista Thymeleaf
- Recordar que usando @RestController tenemos @Controller y @ResponseBody automáticamente

```
  @Controller
  @RequestMapping("/api2")
  public class UserRestController2 {

    @GetMapping("/details")
    @ResponseBody
    public Map<String, Object> details() {

      Map<String, Object> body = new HashMap<>();
      body.put("title", "Hola Mundo Spring Boot");
      body.put("name", "José Manuel");
      body.put("lastname", "Muñoz Manzano");

      return body;
    }
  }
```

## El objeto Model

- Creamos un package llamado models dentro del package principal
- Creamos una clase llamada User dentro del package models
- Usamos un objeto de la clase User en UserRestController, método details2()
- Pasamos a nuestro objeto de la clase Map, como value, nuestro objeto user
- También podríamos devolver directamente el objeto user, no el Map. Para ello, el método debe devolver el tipo User

- Hacemos lo mismo en nuestro controller UserController, para enviar los datos a la vista details3.html
- En details3.html accedemos a los atributos de user de esta forma: `user.name`
- Los atributos son privados, pero Thymeleaf accede a los métodos get, omitiendo la palabra get

## Cómo funciona Spring MVC

- Spring MVC es un framework web basado en MVC y toma ventaja de los siguientes principios
- Modelo, Vista y Controlador
- Inyección de dependencia
- Orientado al uso de interfaces
- Uso de clases POJO

- El Front Controller DispatcherServlet recibe una solicitud HTTP del navegador
- Luego aplica un controlador basado en la URL (Handler mapping) y asigna el request al Controlador
- El controlador se relaciona con componentes de la lógica de negocio y envía datos a la vista usando el objeto Model
- El controlador retorna/asigna el nombre de la vista lógica a mostrar
- Se selecciona un ViewResolver, el cual aplica un determinado tipo de vista (HTML, PDF, Excel...)
- Finalmente, la vista es mostrada al cliente usando los datos del objeto Model

- Clara separación de funciones
- Controlador, validadores, objeto form, DispatcherServlet, handler mapping, view resolver, etc
- Llevan a cabo una tarea específica y pueden ser reemplazados sin afectar a los demás

- Los controladores proporcionan acceso a la lógica de negocio
- Delega la lógica de negocio a un conjunto de componentes de servicios
- Los servicios, a su vez, acceden a las bases de datos mediante la interfaz DAO (objeto de acceso a datos)
- Los controladores reciben parámetros del usuario (input) y lo convierten en un objeto del modelo, poblando en sus atributos los datos enviados, como resultado de la lógica de negocio

## Cambiar puerto

- Fichero application.properties. Indicar el puerto que se quiera, por ejemplo `server.port=8080`
- En mi caso lo dejo como 8080, pero esto es por defecto

## La clase DTO

- Usar clases DTO (Data Transfer Object) es una alternativa a usar el Map en APIs REST
- No se usa tanto para vistas porque renderizamos directamente en el servidor, los datos no viajan al cliente y no mandamos datos sensibles
- En vez de usar un Map para generar el JSON usamos un DTO, que no deja de ser una clase, un POJO, que permite compartir datos
- La clave para entender por qué se usan estas clases DTO es que recuperamos la data de una tabla, pero no queremos compartir toda esa data al cliente (JSON) así que tenemos una clase, nuestro DTO, con aquellos atributos que queremos realmente compartir, y en donde puede haber transformaciones y preparaciones de la data, uniones de distintas tablas a una sola clase DTO...

- Creamos un package dentro del package models, llamado `dto`
- Creamos la clase `UserDto.java`
- En el controlador UserRestController creamos el método `details3()` y trabajamos con nuestro dto en vez de con el Map

## Listas

Vamos a ver como devolver con un JSON una lista de users

- En el controller UserRestController.java, definimos el método GET `list()` y devolvemos un JSON con una lista de users

## Thymeleaf

Nos vamos a centrar en el controller UserController, en el model User y en la plantilla details3.html

### Directiva if

- Uso de la directiva if
  - `<li th:if="${user.email != null}" th:text="${user.email}"></li>`
- Como por defecto evalúa si es distinto de null, también sirve esta forma simplificada
  - `<li th:if="${user.email}" th:text="${user.email}"></li>`
- Y para poner un texto alternativo (el else)
  - `<li th:if="${not (user.email != null)}" th:text="${'No tiene email'}"></li>`
  - `<li th:if="${!(user.email != null)}" th:text="${'No tiene email'}"></li>`
- O también vale
  - `<li th:if="${user.email == null}" th:text="${'No tiene email'}"></li>`
  - Otra forma de indicar el texto: `<li th:if="${user.email == null}">No tiene email</li>`

### Listas en Thymeleaf

- En el controller UserController creamos el método GET `list()` y la plantilla `list.html`
- Se usa th:foreach y se lee "por cada usuario de la lista de usuarios"

```
  <tr th:each="user : ${users}">
    <td th:text="${user.name}"></td>
    <td th:text="${user.lastname}"></td>
    <td th:text="${user.email}"></td>
  </tr>
```

## Anotación @ModelAttribute

- La anotación @ModelAttribute sirve para reutilizar, dentro de un controlador, una lista o cierta información. Es global al controlador
- Se usa bastante en formularios cuando tenemos un controlador para rellenar listas, desplegables, checkboxes que vienen de una lista. El método que se encarga de la petición GET/POST de recuperar/guardar esa información se puede reutilizar
- Entre paréntesis se indica el nombre que luego se usa en la vista, es decir, lo que devuelve este método se guarda en el atributo users en la vista de Thymeleaf.

```
  @ModelAttribute("users")
  public List<User> usersModel() {
    return Arrays.asList(new User("Pepa", "González"),
                         new User("Lalo", "Pérez", "lalo@correo.com"),
                         new User("Juanita", "Roy", "juana@correo.com"),
                         new User("José M.", "Doe"));
  }
```

Y en la vista se hace referencia a users de alguna manera, por ejemplo `<tr th:each="user : ${users}">`

Ahora, automáticamente se recupera la lista de users, y este atributo users puede usarse en cualquier vista que devuelva ese controlador.

## Anotación @RequestParam

- Vamos a ver como pasar parámetros del request (de nuestro usuario) a nuestro controlador usando la anotación `@RequestParam`
- Creamos un nuevo controlador llamado `RequestParamsController.java`, método `foo()`
- Al indicar @RequestParam, estamos esperando recibir un parámetro de la ruta URL con el nombre indicado, message en el ejemplo
- Se tienen que indicar tipos de parámetros primitivos, o sus versiones objeto. Para pasar objetos, lo hacemos como JSON, pero no por URL, sino en el request body (luego lo veremos)
- Si indicamos que el tipo es un Integer, pero en la URL escribimos texto, dará un error que no se puede manejar con try-catch, aunque si es posible manejar la excepción de otra forma que veremos más adelante
- Para evitar esto también se puede usar la versión nativa HttpServletRequest (ver más abajo)
- En principio, es obligatorio indicar el parámetro en la ruta url, salvo que indiquemos en la configuración de @RequestParam que es opcional
  - `@RequestParam(required = false) String message`
- También se puede indicar un valor por defecto
  - `@RequestParam(required = false, defaultValue = "Hola!!") String message`
- También podemos indicar un nombre del parámetro de URL distinto al que luego se usa en el método, aunque por convención suelen llamarse igual
  - `@RequestParam(required = false, defaultValue = "Hola!!", name = "mensaje") String message`

```
  @GetMapping("/foo")
  public ParamDto foo(@RequestParam String message) {

    ParamDto param = new ParamDto();
    param.setMessage(message);

    return param;
  }
```

### Obtener varios parámetros de la URL

- Ver controlador `RequestParamsController.java`, método `bar()`
- Cuando viene más de un parámetro en la URL, estos se separan con &
- A nivel de código Java, simplemente se añaden tantos @RequestParam como parámetros se esperan

```
  @GetMapping("/bar")
  public ParamDto bar(@RequestParam String text, @RequestParam Integer code) {

    ParamDto params = new ParamDto();
    params.setMessage(text);
    params.setCode(code);

    return params;
  }
```

## Objeto request desde HttpServletRequest

- En vez de utilizar la anotación @RequestParam, podemos usar la forma nativa, inyectando un objeto del tipo `HttpServletRequest`
- Ver controlador `RequestParamsController.java`, método `bar()`
- `request.getParameter("atributoURL")` se devuelve siempre como un String
- Los parámetros de la URL se pueden omitir, pero una transformación de un null a Integer, o un String a Integer dará error, así que tenemos que validar los datos

```
  @GetMapping("/request")
  public ParamMixDto request(HttpServletRequest request) {

    Integer code = 0;

    try {
      code = Integer.parseInt(request.getParameter("code"));
    } catch (NumberFormatException e) {
      // Por defecto code vale 0
    }

    ParamMixDto params = new ParamMixDto();
    params.setCode(code);
    params.setMessage(request.getParameter("message"));

    return params;
  }
```

## Anotación @PathVariable

- La anotación @PathVariable es la otra forma de enviar parámetros a nuestra app usando la ruta
- Creamos un nuevo controlador llamado `PathVariableController.java`, método `baz()`
- Indicamos en @GetMapping, en la ruta, un parámetro variable entre llaves `{variable}`
- Luego usamos @PathVariable e indicamos un nombre igual al indicado en la ruta, en el ejemplo, message
- Se puede indicar como configuración si es requerido o no. Si no es requerido, debemos indicar también la ruta sin el path variable
  - ```
    @GetMapping({"baz", "/baz/{message}"})
    public ParamDto baz(@PathVariable(required = false) String message) {}
    ```
- Lo normal cuando trabajamos con API REST es mandar parámetros en la ruta en vez de parámetros de request
- También podemos cambiar el nombre del PathVariable: `@PathVariable(required = false, name = "mensaje") String message` pero entonces, en nuestro @GetMapping también tenemos que indicar el mismo nombre: `@GetMapping({"baz", "/baz/{mensaje}"})`

```
  @GetMapping("/baz/{message}")
  public ParamDto baz(@PathVariable String message) {

    ParamDto param = new ParamDto();
    return param;
  }
```

### Obtener varios parámetros de la ruta

- Ver controlador `PathVariableController.java`, método `mix()`
- Se añaden tantos @PathVariable como parámetros se esperan
- Si indicamos que alguno no es requerido, tenemos que indicar también, entre llaves, dos rutas, aquella en la que no viene el parámetro y aquella en la que si viene

```
  @GetMapping("/mix/{product}/{id}")
  public Map<String, Object> mixPathVariable(@PathVariable String product, @PathVariable Long id) {

    Map<String, Object> json = new HashMap<>();
    json.put("product", product);
    json.put("id", id);

    return json;
  }
```

## APIREST Enviar JSON petición POST

- Ver controlador `PathVariableController.java`, método `create()`
- Se utiliza la anotación @PostMapping
- Y como argumento, tenemos que anotar con @RequestBody para esperar un JSON
- Para que el ejemplo indicado abajo funcione, la clase User tiene que tener creados el constructor por defecto (si hay otro) y los getter/setter

```
  @PostMapping("/create")
  public User create(@RequestBody User user) {

    // Hacer algo con el usuario, como un save en la BBDD
    return user;
  }
```

## Inyectar valores usando la anotación @Value

- La idea es externalizar la configuración de nuestra aplicación
- En el archivo properties tendremos configuraciones que podremos utilizar en nuestros controladores, inyectándolos
- Hay dos formas de hacer esto
  - Una es compatible con el fichero application.properties, donde configuraremos cosas propias del framework y propias nuestras para el desarrollo. Ejemplo: `config.code=23232`
- En nuestro controlador `PathVariableController.java` creamos atributos y los anotamos con `@Value("${property}")`
- Solo podemos usar @Value en componentes Spring, es decir que han sido anotados con @Service, @Controller...

```
  @Value("${config.code}")
  private Integer code;
```

- También creamos un método `values()` donde usamos nuestros atributos con valores provenientes de la configuración
- Y también se puede pasar como argumento la anotación @Value al método handler

```
  @GetMapping("/values")
  public Map<String, Object> values(@Value("${config.message}") String message) {

    Map<String, Object> json = new HashMap<>();
    json.put("username", username);
    json.put("code", code);
    json.put("message", message);
    json.put("listOfValues", listOfValues);

    return json;
  }
```

## Agregando otros archivos properties personalizados para las configuraciones

- En la carpeta resources podemos crear nuestros propios archivos properties personalizados
- Tienen que acabar en .properties
- Ejemplo `values.properties`
- Para poder usarlo en nuestra app, hay un par de alternativas:
  - 1. En nuestra clase principal SpringbootWebApplication.java, aprovechando que es también una clase con la anotación @Configuration
    - Tras la anotación @SpringBootApplication escribiremos: `@PropertySource("classpath:values.properties")`
    - Y luego se inyecta con la anotación `@Value` como ya hemos visto
    - Si hay más de un archivo personalizado, la anotación es: `@PropertySources({@PropertySource("classpath:values.properties")})`
  - 2. En una clase personalizada de configuración, por ejemplo `ValuesConfig.java` en la raiz del proyecto, anotándola con `@Configuration`
    - Tras la anotación escribiremos, como en la opción 1 `@PropertySource` o `@PropertySources`, dependiendo de si hay uno o más archivos personalizados .properties
    - Con esta opción evitamos "ensuciar" nuestra clase principal

## Encoding charset en properties

- Para que nos funcionen las tildes, eñes... en el fichero .properties hay varias opciones:
  - Añadir el encoding a la anotación @PropertySource, de la siguiente forma:
    - `@PropertySource(value = "classpath:values.properties", encoding = "UTF-8")`
  - Si el fichero es UTF-8, escapando caracteres Unicode en el fichero .properties: `config.message=Hola que tal como est\u00e1s`
  - Con VSCode, seleccionamos nuestro fichero .properties, pulsamos abajo de VSCode en UTF-8 y seleccionamos `Save with Encoding`. Luego seleccionados `Western (ISO 8859-1)` Esta es la peor opción

## Spring Expression Language (SpEL) con @Value

- Dentro de @Value podemos manipular el lenguaje de expresión de Java
- Para indicar que es un lenguaje de expresión escribimos #{} y dentro de las llaves podemos poner código Java: `@Value("#{${config.listOfValues}}")`
- Ver PathVariableController.java para un ejemplo un poco más completo

### Map SpEL con @Value

- Se incluye en el fichero values.properties una propiedad que es un objeto JSON que vamos a inyectar en nuestro controlador Spring PathVariableController.java usando @Value, pero como un Map de Java
- En una sola línea podemos tener un objeto de configuraciones
- Ejemplo: `config.valuesMap={product: 'Cpu Intel Core i7 12th', description: 'Alder Lake, 12 core, a 5 GHz', price: '1000'}`
- Y para inyectarlo:
- ```
    @Value("#{${config.valuesMap}}")
    private Map<String, Object> valuesMap;
  ```
- Si quisiéramos pasar solo un atributo del JSON, por ejemplo, price:
- ```
    @Value("#{${config.valuesMap}.price}")
    private Long price;
  ```
- Se convierte de un Map a un JSON cuando lo devolvemos en un controlador

## Objeto Environment de Spring para leer configuraciones

- Alternativa al uso de la anotación @Value
- Consiste en inyectar, usando @Autowired, el componente Spring Environment
- Hay un ejemplo en el controlador PathVariableController.java
- ```
    @Autowired
    private Environment environment;
  ```
- Usándolo, podemos leer los archivos properties que tenemos configurados
- La forma de usarlo es a través del método getProperty(), que nos permite incluso indicar un valor por defecto si la propiedad no existe
  - `json.put("message", environment.getProperty("config.message"));`
- Todo lo devuelve como String, así que si es necesario devolver otro tipo de dato:
  - Se puede usar el método valueOf() y un valor por defecto, porque podría ser null
    - `json.put("code2", Integer.valueOf(environment.getProperty("config.code", "0")));`
  - Se puede usar el método getProperty() para indicar el tipo al que se quiere convertir
    - `json.put("code2", environment.getProperty("config.code", Long.class));`

## Retornando redirect y forward como respuesta en métodos del controlador

- Para uso de Spring MVC con Thymeleaf (no REST)
- redirect sirve para redirigir de una página a otra
- Ejemplo realizado en HomeController.java
- Se suele usar tras haber dado de alta datos de un formulario (POST), damos de alta y redirigimos a otra página que muestra el listado donde ya aparece el nuevo item
- ```
    @GetMapping({"", "/", "/home"})
    public String home() {
      return "redirect:/list";
    }
  ```
- Se pierde todo el request

- forward hace un dispatcher
- Por debajo maneja el API Servlet `request.getRequestDispatcher("/details").forward(request, response)` que redirige de un servlet a otro, o de un JSP a otro
- Nuestro forward redirige hacia rutas de controladores (métodos handlers)
- Ejemplo:
- ```
    @GetMapping("/forward")
    public String home_forward() {
      return "forward:/det";
    }
  ```

- La diferencia entre forward y redirect es que con el forward nos mantenemos dentro de la misma petición http, por lo que no se pierden los parámetros que tenemos dentro del request. Tampoco cambia la ruta url, ya que no hace un refresh de la página, sino que despacha a otra acción del controlador, pero sin recargar la página. El redirect si cambia la ruta url, reinicia el request y refresca el navegdor, además que todos los parámetros del request que teníamos antes del redirect se pierden en este nuevo request

### Directiva href (enlaces) de Thymeleaf

- href sirve para crear rutas de una vista Thymeleaf a otra vista Thymeleaf
- Vamos a hacer un ejemplo en la plantilla details3.html
- Se usa th:href y se indica @{} para las rutas. Las rutas siempre comienzan con /
  - `<a th:href="@{/list}">Ver Listado</a>`
- Se pueden enviar parámetros de las siguientes formas:
  - Concatenando usando la comilla simple. Se usa si tenemos variables
    - `<a th:href="@{'/api/params/foo?mensaje=' + ${title}}">Ver Mensaje</a>`
  - Sin variables
    - `<a th:href="@{/api/params/foo?mensaje=Hola que tal festival}">Ver Mensaje2</a>`
  - Forma propia de Thymeleaf, usando paréntesis y dentro el nombre de la propiedad y su valor
    - `<a th:href="@{/api/params/foo(mensaje='Hola que tal festival')}">Ver Mensaje3</a>`
  - Con múltiples parámetros
    - `<a th:href="@{/api/params/bar(text='Hola que tal festival', code=12345)}">Ver Mensaje4</a>`
  - Para el caso de los Path Variables
    - `<a th:href="@{/api/var/baz/Esta es la parte variable}">Ver Mensaje5</a>`
  - O si se usa una variable en vez de un literal
    - `<a th:href="@{'/api/var/baz/' + ${title}}">Ver Path Variable 2</a>`

## Despliegue y ejecución desde terminal (deploy)

- Vamos a desplegar, pero en local
- La carpeta target no debe tener ningún archivo .jar
- Nos vamos al root de nuestro proyecto
- Ahí tenemos instalado ya maven como parte de la creación del proyecto
  - mvnw para Linux y Mac
  - mvnw.cmd para Windows
- Hay dos posibilidades:
  - Ejecutamos `./mvnw install` para empaquetar o generar nuestro .jar y además lo instala en el repositorio local de nuestro Maven (carpeta .m2 donde están las dependencias de Maven)
    - Como lo deja en nuestro repositorio local (.m2) lo podríamos usar en otro proyecto
    - En este caso, siendo una app SpringBoot no es reutilizable
  - Ejecutamos `./mvnw clean package` para empaquetar nuestra aplicación, es decir, generar el fichero .jar
    - Ejecutamos este comando
- Vamos a la carpeta target y deberemos ver un archivo .jar
- Hay otro archivo .jar.original que NO nos interesa
- Ejecutamos el proyecto `java -jar springboot-web-0.0.1-SNAPSHOT.jar`
- Vamos al navegador o a Postman y accedemos a cualquiera de las rutas

## Deploy Maven desde Visual Studio Code

- Forma de hacer el deploy integrada en el IDE
- En VSCode, nos vamos a la pestaña Maven (en la parte izquierda)
- Abrimos el proyecto. Abrimos Lifecycle
- Ejecutamos clean
- Ejecutamos package
- Vamos a la carpeta target y deberemos ver un archivo .jar
- Ejecutamos el proyecto `java -jar springboot-web-0.0.1-SNAPSHOT.jar`
- Vamos al navegador o a Postman y accedemos a cualquiera de las rutas

### Detalle con Maven Clean

- Una vez ejecutamos, usando la utilidad de Maven integrada en VSCode, el Lifecycle clean, o usando el comando ./mvnw clean:
- Ejecutamos el Reload All Maven Projects (aparece en la misma pestaña Maven, el círculo con flechas en ambos sentidos)
- Esto se hace porque si no, si volvemos a ejecutar el proyecto desde VSCode, en desarrollo, nos indica que no se ha encontrado o cargado la clase principal

## Ejecución de la app

Ejecutar el proyecto e ir a las siguientes rutas:

- Hola Mundo Spring Boot: `http://localhost:8080/det`
- Pasar datos a una vista usando Model: `http://localhost:8080/details`
- Pasar datos a una vista usando Map: `http://localhost:8080/details2`
- Rest Controller usando @RestController: `http://localhost:8080/api/details`
- Rest Controller usando @Controller y @ResponseBody: `http://localhost:8080/api2/details`
- Rest Controller usando objeto Model: `http://localhost:8080/api/details2`
- Controller usando objeto Model (vista): `http://localhost:8080/details3`
- Rest Controller usando DTO (API): `http://localhost:8080/api/details3`
- Rest Controller devolviendo una lista: `http://localhost:8080/api/list`
- Thymeleaf directiva if: `http://localhost:8080/details3`
- Thymeleaf listas: `http://localhost:8080/list`
- @RequestParam: `http://localhost:8080/api/params/foo?mensaje=Hola que tal`
- Varios @RequestParam: `http://localhost:8080/api/params/bar?text=Hola que tal&code=20`
- HttpServletRequest: `http://localhost:8080/api/params/request?message=Usando HttpServletRequest&code=30`
- @PathVariable: `http://localhost:8080/api/var/baz/Hola usando @PathVariable`, `http://localhost:8080/api/var/baz`
- Varios @PathVariable: `http://localhost:8080/api/var/mix/IPhone 15/123456`
- API REST Enviar JSON petición POST:
  - Usando Postman acceder a `http://localhost:8080/api/var/create`
  - JSON enviado: `{"name":"Pepe", "lastname":"Doe", "email":"pepe@correo.com"}`
- Inyectar valores usando la anotación @Value. En Postman, GET: `http://localhost:8080/api/var/values`
- Agregando otros archivos properties personalizados para las configuraciones. En Postman, GET: `http://localhost:8080/api/var/values`
- Spring Expression Language (SpEL) con @Value. En Postman, GET: `http://localhost:8080/api/var/values`
- Map SpEL con @Value. En Postman, GET: `http://localhost:8080/api/var/values`
- Objeto Environment de Spring para leer configuraciones. En Postman, GET: `http://localhost:8080/api/var/values`
- Retornando redirect y forward como respuesta en métodos del controlador. Redirect: `http://localhost:8080`
- Retornando redirect y forward como respuesta en métodos del controlador. Forward: `http://localhost:8080/forward`
- Directiva href (enlaces) de Thymeleaf: `http://localhost:8080/details3` y pulsar en los enlaces
