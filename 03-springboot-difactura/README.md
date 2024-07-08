# INYECCION DE DEPENDENCIAS - EJEMPLO FACTURA

Un proyecto de ejemplo de factura para poder practicar las relaciones de objetos mediante la inyección de dependencias

## Models creados

- Client
- Invoice
  - Se relaciona con client y una lista de items
- Item
  - Se relaciona con product
  - Un Item tiene un solo producto
- Product
  - Puede estar asociado a muchos items

Las clases Cliente e Invoice van a ser componentes. Item y Product no.

Un item, por si solo, no puede ser un componente. Es la lista de items lo que será el componente.

Pero no podemos anotar una instancia de tipo List con @Component, porque es una interface `externa` de Java.

Crearemos en una clase de configuración un método que devolverá una `List<Item>` y lo anotaremos con @Bean. Ahí crearemos los productos. por eso tampoco la clase product es un componente, porque los creamos aquí.

El conjunto `List<Item>` y Product será el componente que devuelve el método en la clase de configuración.

Es entonces cuando se puede inyectar en Invoce usando @Autowired.

## Qué temas se tratan

- Creación de modelos POJO
- @Component
- @Autowired
- Fichero de properties
- Clase de configuración anotada con @Configuration
- @PropertySource
- Encoding de properties UTF-8
- @Value
- @Bean
- Creación de clase controlador
- Método handler
- @RestController
- @RequestMapping
- @GetMapping
- @Primary
- @Qualifier
- @PostConstruct: Método del ciclo de vida que se ejecuta justo después de crearse el componente en el contexto de la app
  - Son métodos public y void, sin argumentos
  - La diferencia con usar un constructor es que en el constructor los atributos inyectados no tienen el valor todavía, tienen el valor null. Lo adquieren después de hacer el new. En un método anotado con @PostConstruct, el objeto ya se ha instanciado y se pueden manipular los atributos
- @PreDestroy: Método del ciclo de vida que se ejecuta justo antes de destruirse el componente bean en su contexto
  - Son métodos public y void, sin argumentos
  - Se suele cerrar algún recurso, eliminar datos de BBDD, cerrar conexiones de BBDD (aunque Spring lo maneja automáticamente), ...
- @RequestScope
- @JsonIgnoreProperties: atributos a ignorar a la hora de crear el JSON
  - Una alternativa, que se ve en el controlador, método show, consiste en crear una nueva instancia de Invoice y de Cliente (aunque a mi no me falla si no hago la nueva instancia de Cliente), y pasarle los datos que necesitamos, devolviendo esa instancia y evitando el proxy. Esto podría ser una clase DTO
- API Stream
- Uso de Postman

## Testing en Postman

- Ruta: `http:\\localhost:8080\invoices/show`
