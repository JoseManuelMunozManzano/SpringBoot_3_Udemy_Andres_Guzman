# Spring Security JWT - Bidireccional

Vamos a ver autenticación JWT, basado en token para API Rest.

Para este ejemplo, se reutiliza el proyecto `20-springboot-crud-jwt`, pero aquí tratamos ManyToMany Bidireccional entre los entities User y Role.

Esto lo hacemos, en concreto ,para ver el error de bucle infinito entre User y Role, ya que no es correcto que esta relación sea bidireccional ya que no necesitamos listar los roles con sus usuarios. Esto al menos para este proyecto, ya que es posible que en otros proyectos si tenga sentido.

## Qué temas se tratan

Solo se indica lo realmente nuevo con respecto al proyecto `20-springboot-crud-jwt`.

- Entidad
  - @ManyToMany
    - mappedBy
  - @JsonIgnoreProperties, muy importante para evitar los bucles circulares
  - hashCode()
  - equals()

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

Creamos las tablas de forma manual:

```
CREATE TABLE users (
 id BIGINT NOT NULL AUTO_INCREMENT,
 username VARCHAR(18) NOT NULL,
 password VARCHAR(60) NOT NULL,
 enabled TINYINT NOT NULL DEFAULT 1,
 PRIMARY KEY (id)
);

ALTER TABLE users ADD UNIQUE INDEX username_UNIQUE (username ASC) VISIBLE;

CREATE TABLE roles (
 id BIGINT NOT NULL AUTO_INCREMENT,
 NAME VARCHAR(45) NOT NULL,
 PRIMARY KEY (id)
);

ALTER TABLE roles ADD UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE;

CREATE TABLE users_roles (
 user_id BIGINT NOT NULL,
 role_id BIGINT NOT NULL,
 PRIMARY KEY (user_id, role_id)
);

ALTER TABLE users_roles
ADD CONSTRAINT FK_users FOREIGN KEY (user_id) REFERENCES users(id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT FK_roles FOREIGN KEY (role_id) REFERENCES roles(id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

INSERT INTO roles (name) values ('ROLE_ADMIN');
INSERT INTO roles (name) values ('ROLE_USER');
```

En Postman probar la siguiente ruta:

- Users (GET público): http://localhost:8080/api/users

Esto genera un bucle infinito. ¿Por qué?

Se genera el objeto JSON a partir de la lista de usuarios. A su vez cada usuario tiene una lista de roles.

Entonces se genera el objeto JSON a partir de la lista de los roles. A su vez cada rol tiene una lista de usuarios.

Entonces se genera el objeto JSON a partir de la lista de usuarios. A su vez cada usuario tiene una lista de roles.

... y así se produce este bucle circular infinito hasta que revienta la aplicación.

La solución es, en uno de los JSON, excluir la contraparte, usando la anotación @JsonIgnoreProperties para romper el bucle cíclico.

Esta anotación permite ignorar ciertos atributos, en este caso, en la clase User.java, del objeto Role vamos a excluir la lista de usuarios.

`@JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})`

Notar que también estamos ignorando los atributos "handler" y "hibernateLazyInitializer". Esto es porque a veces se produce un error muy raro, porque se genera un proxy cuando va a buscar los roles y se genera el JSON, se invoca el método getRoles() pero haciendo una consulta lazy (carga perezosa) y, como es un proxy, tiene otros atributos que son basura, y hay veces que esos atributos no se eliminan y provocan un error en la serialización del JSON.

Es buena práctica, indicar la anotación @JsonIgnoreProperties en ambos lados de la relación, es decir, también en la clase Role.java
