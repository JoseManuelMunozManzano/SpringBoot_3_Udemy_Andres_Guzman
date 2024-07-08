# INYECCION DE DEPENDENCIAS (IoC)

Características que se ven en esta app REST:

- NO HAY INYECCION DE DEPENDENCIA EN ESTA APP

- Hemos creado packages para controllers, models, services y repositories
  - El controlador se encarga de manejar los métodos handler, recibir las peticiones de los usuarios y devolver la respuesta (vista/JSON)
  - En nuestro package models creamos nuestras clases POJO (atributos, constructores, setters y getters) que puede estar mapeada a una tabla en BBDD
  - Los services interactúan con los repositorios y contienen la lógica de negocio
  - Los repositorios son los que acceden a los datos
- Tras terminar este ejercicio, todo queda muy acoplado

## Creando la clase Model y la clase Repository

- En el package models, creamos el POJO Product.java
- En el package repositories, creamos ProductRepositoryImpl.java
  - Es la capa de acceso a datos (BBDD, un JSON...)
  - Lo implementamos, por ahora, sin interfaces y sin inyección de dependencia, de forma simple
  - Va a tener data y métodos para buscar un producto por id y devolver todos los productos
  - La clase repository se inyecta/utiliza en la capa service

## Escribiendo la clase Service

- En el package services, creamos ProductServiceImpl.java
  - Tendrá los mismos métodos que ProductRepositoryImpl.java, y el objetivo es obtener los datos utilizando el repositorio para manipularlos (cálculos, transacciones...) en función de lo que dicte nuestra lógica de negocio
  - La lógica de negocio no queda acoplada en el controlador
  - La clase service se inyecta/utiliza en la capa de controlador

## Creando la clase Controller

- En el package controllers, creamos SomeController.java
  - La anotamos con @RestController y @RequestMapping("/api")
  - Creamos los métodos list() y show()

## Principio de inmutabilidad

Indicar que todos los problemas que vemos aquí ahora mismo suceden porque nuestro Repository está en memoria

- Inmutabilidad consiste en evitar modificar el objeto original
- El problema viene porque inyectamos el servicio en el controlador como un atributo, y el `contexto/scope del controlador es singleton` por defecto, es decir, solo hay una instancia compartida por toda la aplicación y todos los usuarios
- `Los métodos handler del controlador son por request`, es decir, se ejecutan por cada usuario y por cada petición de forma atómica
- Pero como tenemos el atributo service fuera de los métodos handler, la data muta en cada petición, sin reiniciarse
- Para solucionar esto, podríamos llevar la inicialización del service a cada handler, pero eso es una mala práctica, ya que se duplica código
- Realmente, tenemos que tener cuidado, usando map en ProductServiceImpl.java, método findAll(), de devolver un nuevo producto en vez de mutar el que estamos tratando

### Inmutabilidad usando la interface Cloneable

- Forma más fina de manejar la inmutabilidad
- Modificamos la clase Product.java (en el package models) para que implemente Cloneable
- Tenemos que sobreescribir el método clone() y, luego, en el método findAll() de la clase ProductServiceImpl.java usarlo para generar un clon de tipo Product (usando el casting), y sobre el, modificar el precio

## Escribiendo las interfaces Repository y Service

- Hacemos las interfaces (contratos) de nuestras clases ProductRepositoryImpl.java y ProductServiceImpl.java, y estas clases implementarán dichas interfaces, indicando la anotación @Override
- Las nuevas interfaces se llaman respectivamente ProductRepository.java y ProductService.java
- Son necesarias para la inyección de dependencias, ya que la idea es inyectar el tipo genérico y no su implementación
- La idea es que podemos tener distintas implementaciones de estos contratos y podremos inyectar el que necesitemos

## Testing

Usamos Postman

- Listar todos los productos. GET: `http:\\localhost:8080\api`
- Buscar por producto. GET: `http:\\localhost:8080\api\1`
