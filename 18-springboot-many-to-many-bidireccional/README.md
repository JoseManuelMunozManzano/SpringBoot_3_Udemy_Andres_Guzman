# @ManyToMany

Veremos un ejemplo de relación @ManyToMany bidireccional donde configuramos el nombre de nuestra tabla intermedia.

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
- @ManyToMany
  - cascade
  - mappedBy
- @Transactional
- saveAll
- @JoinTable
  - @JoinColumns
  - @UniqueConstraints
- findById
- Optional
  - isPresent()
- hashCode()
- equals()
- @Query
  - left join fetch

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
SELECT * FROM courses;
SELECT * FROM students;
SELECT * FROM tbl_alumnos_cursos;
```

Es una aplicación de consola, por lo que el resultado se ve en ella.
