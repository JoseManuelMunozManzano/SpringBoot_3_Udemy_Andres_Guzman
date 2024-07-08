# @OneToMany EN RELACIÓN EXISTENTE

Veremos un ejemplo de relación @OneToMany donde, en vez de crear una tabla intermedia, usaremos @JoinColumn para crear el campo de la foreign key en la tabla addresses.

## Qué temas se tratan

- @Entity
- @Table
- @Id
- @GeneratedValue(strategy = GenerationType.IDENTITY)
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
- @JoinColumn
- @OneToMany
  - cascade
  - orphanRemoval
- Creación de campo de foreign key en tabla de la relación ya existente
- Optional
  - ifPresent
- findById

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

Si existen, para empezar desde cero ejecutar:

```
DROP TABLE clients;
DROP TABLE addresses;
DROP TABLE clients_addresses;
DROP TABLE invoices;
```

En la ruta src/main/resources se encuentra un script llamado import.sql con datos a insertar en las tablas. Spring lo ejecutará automáticamente.

Ejecutar el proyecto y en Squirrel ejecutar:

```
SELECT * FROM clients;
SELECT * FROM addresses;
```

En la tabla addresses veremos que existe un campo foreign key llamado client_id.

Es una aplicación de consola, por lo que el resultado se ve en ella.
