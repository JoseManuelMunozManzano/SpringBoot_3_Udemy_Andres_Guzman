# JPA E HIBERNATE - JPQL

Vamos a ver la parte de JPA e Hibernate que se refiere a JPA Query Language (JPQL)

Todas consultas personalizadas.

IMPORTANTE: Es ejecución de línea de comandos. Para que SpringBoot finalice tras la ejecución en el POM no debe estar la dependencia Web.

## Qué temas se tratan

- Bean CommandLineRunner e implementamos el método run()
- Clase Entity anotada con @Entity para indicar que es una clase de persistencia y con @Table para cambiar el nombre de la tabla que mapea
- Mapeo de atributos de clase a campos de tabla de BBDD
  - Id usando anotaciones @Id y @GeneratedValue(strategy = GenerationType.IDENTITY) y demás campos usando anotación @Column
- Interface Repository
  - El nombre suele ser XxxxxRepository.java o XxxxxDao.java de acceso a datos que extiende de CrudRepository
- @Autowired
- Configurando propiedades de conexión a la BBDD y de JPA
  - En application.properties
  - ```
    spring.datasource.url=jdbc:mysql://localhost:3306/db_springboot
    spring.datasource.username=root
    spring.datasource.password=sasa1234
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
    spring.jpa.show-sql=true
    ```
- Forma automática de crear tablas usando la configuración
  - En application.properties
  - ```
    # Creación de todo el lenguaje de definición de datos (DDL) de forma automática
    # Con el valor create, al arrancar el proyecto lee toda la metadata (anotaciones)
    # de nuestra clase entity, si existen las tablas las elimina y luego las crea de nuevo.
    # IMPORTANTE: SOLO PARA DESARROLLO Y TESTING!!
    spring.jpa.hibernate.ddl-auto=create
    # Y una vez se ha creado la tabla y ejecutado el script import.sql, para que no se
    # esté borrando/creando la tabla y la data, cambiarlo a update
    # Con update, si se crea un campo nuevo o una tabla nueva, si hace la creación de ese campo o tabla nueva.
    # IMPORTANTE: SOLO PARA DESARROLLO Y TESTING!!
    spring.jpa.hibernate.ddl-auto=update
    ```
- Ejecución automática de fichero import.sql en /src/main/resources con data para inicializar tablas
- Consultas personalizadas con anotación @Query
- Devolviendo campos personalizados
- Devolviendo un mix de objeto y campos personalizados
- Constructor en Entity con menos campos para poder devolver un objeto persona con esos campos
- Instanciación dinámica de clase DTO personalizada
- Creación de clase DTO (Data Transfer Object)
- Usando palabras clave de JPQL/HQL
  - distinct
  - count
  - concat
  - upper
  - lower
  - like
  - between (@Query y Query Method)
  - order by (@Query y Query Method)
  - agregación
    - count
    - max
    - min
    - sum
    - avg
  - length
- Subconsultas
- Operador where in
- @Transactional anotado en métodos que hacen inserciones, actualizaciones y borrados en BBDD
- @Transactional(readOnly = true) en métodos que hacen lecturas de BBDD
- Uso de Scanner

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

En Squirrel crear el siguiente alias

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

En application.properties, descomentar `spring.jpa.hibernate.ddl-auto=create` y comentar `spring.jpa.hibernate.ddl-auto=update`

En la ruta src/main/resources se encuentra un script llamado import.sql con datos a insertar para la tabla persons (en minúscula)
Spring lo ejecutará automáticamente.

Ejecutar el proyecto y en Squirrel ejecutar: `SELECT * FROM persons;`

En application.properties, comentar `spring.jpa.hibernate.ddl-auto=create` y descomentar `spring.jpa.hibernate.ddl-auto=update`, para que en las siguientes ejecuciones no borre la tabla/data existente.

Es una aplicación de consola, por lo que el resultado se ve en ella.

Hace inserciones/actualizaciones/borrados en BBDD usando la clase Scanner, es decir, que espera datos.
