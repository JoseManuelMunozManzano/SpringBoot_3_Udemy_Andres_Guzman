# ASOCIACIONES

Vamos a ver las relaciones (primary keys, foreign keys) entre las clases Entity usando JPA.

Usando JPA, las relaciones son a nivel de atributos. Cada clase Entity va a estar mapeada con distintos tipos de cardinalidad:

- @ManyToOne
- @OneToMany
- @OneToOne
- @ManyToMany

Para @Many utilizaremos una lista de objetos.

## Qué temas se tratan

- @Entity
- @Table
- @Id
- @GeneratedValue(strategy = GenerationType.IDENTITY)
- @ManyToOne
- @CrudRepository
- Configuración de acceso a BBDD en application.properties
  - ```
      spring.datasource.url=jdbc:mysql://localhost:3306/db_springboot
      spring.datasource.username=root
      spring.datasource.password=sasa1234
      spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
      spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
      spring.jpa.show-sql=true
      spring.jpa.hibernate.ddl-auto=create
    ```
- Fichero import.sql con inserciones
- @Bean
- CommandLineRunner
- @Transactional
  - Importación: `import org.springframework.transaction.annotation.Transactional;`
- save()
- findById()
- Optional
- isPresent()
- orElseThrow()
- @JoinColumn
- @OneToMany
  - cascade
  - orphanRemoval
- Creación de tabla intermedia

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

En la ruta src/main/resources se encuentra un script llamado import.sql con datos a insertar en las tablas. Spring lo ejecutará automáticamente.

Ejecutar el proyecto y en Squirrel ejecutar:

```
SELECT * FROM clients;
SELECT * FROM invoices;
SELECT * FROM addresses;
SELECT * FROM clients_addresses;
```

En la tabla invoices veremos que existe un campo client_id que apunta al id de la tabla clients.

La tabla clients_addresses es una tabla intermedia que se crea gracias a la relación @OneToMany

Es una aplicación de consola, por lo que el resultado se ve en ella.
