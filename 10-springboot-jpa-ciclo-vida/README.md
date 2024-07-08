# JPA CICLO DE VIDA

Vamos a ver eventos del ciclo de vida del objeto entity de persistencia.

IMPORTANTE: Es ejecución de línea de comandos. Para que SpringBoot finalice tras la ejecución en el POM no debe estar la dependencia Web.

Cuando queremos hacer alguna tarea o guardar algún dato justo antes de que se persista o se guarde, o actualice en BD, como por ejemplo, una fecha de actualización o usuario de actualización usaremos el ciclo de vida.

Tenemos anotaciones que nos permiten usar dicho ciclo de vida.

- Al cargar por ID
  - @PreLoad
  - @PostLoad
- Al guardar
  - @PrePersist
  - @PostPersist
- Al actualizar
  - @PreUpdate
  - @PostUpdate
- Al eliminar
  - @PreRemove
  - @PostRemove

Típicamente se suelen usar los Pre para auditar la tabla y el Post para hacer algún tipo de limpieza.

En el Entity Person creamos un método que debe devolver void. Lo hemos llamado prePersist y lo anotamos con @PrePersist.

Usaremos las anotaciones `@Embedded` y `@Embeddable` para reutilizar código.

Podríamos tener muchas clases Entity que trabajen conjuntas, que estén relacionadas, o que no trabajen juntas, pero que tengan campos en común.

La idea es reutilizar los campos en diferentes clases Entity.

Vamos a hacer una clase separada y a desacoplar las fechas de los otros Entity, creándolos en esa clase separada, para poder reutilizar dichas fechas. Esta clase será integrable, acopable a las clases Entity. Usaremos para ello las anotaciones antes indicadas @Embedded y @Embeddable.

Creamos una clase Audit que anotamos, en vez de con @Entity, con @Embeddable. Nos llevamos ahí todos los campos que sean susceptibles de reutilizarse, con sus getter/setter y, si tiene, con sus métodos del ciclo de vida.

En la clase Person, crearemos un atributo de tipo Audit y lo anotamos con @Embedded.

## Qué temas se tratan

- Clase Entity
- Clase Dto
- Clase Repository
- CommandLineRunner
- @PrePersist
- @PreUpdate
- @Embeddable
- @Embedded
- @Formula

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
