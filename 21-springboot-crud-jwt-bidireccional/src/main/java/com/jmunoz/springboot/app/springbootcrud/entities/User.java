package com.jmunoz.springboot.app.springbootcrud.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @NotBlank
  @Size(min = 4, max = 12)
  private String username;
  
  // El password no lo debe mostrar en una petición GET.
  // Para solucionar esto podemos:
  // 1. Crear una clase DTO, que es lo que se envía al usuario, excluyendo el envío del password.
  // 2. Ignorar el atributo en el JSON. Se usa @JsonProperty y establecemos
  //    WRITE_ONLY, es decir, solo damos acceso a esta propiedad cuando se escribe, es decir,
  //    tomamos el JSON y lo escribimos en una clase, pero NO cuando tomamos una clase y la pasamos
  //    a un JSON.
  // 3. Usando @JsonIgnore, pero para este caso en concreto no vale porque lo quita del JSON tanto
  //    para leer como para escribir. 
  @NotBlank
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  // @JsonIgnore
  private String password;

  // Relación bidireccional. Ver Role.java
  // Para evitar un bucle cíclico hay que indicar @JsonIgnoreProperties, que lo que hace es
  // ignorar ciertos atributos del objeto Role, en este caso queremos ignorar los usuarios.
  // Con esto rompemos el bucle circular.
  // Notar que también estamos ignorando los atributos "handler" y "hibernateLazyInitializer"
  // Esto es porque a veces se produce un error muy raro, porque se genera un proxy cuando va a buscar
  // los roles y se genera el JSON, se invoca el método getRoles() pero haciendo una consulta lazy (carga perezosa)
  // y, como es un proxy, tiene otros atributos que son basura, y hay veces que esos atributos no se eliminan,
  // y provocan un error en la serialización del JSON
  // Si queremos que el programa falle, solo tenemos que comentar esta línea y el @JsonIgnoreProperties
  // de la clase Role.java
  @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})
  @ManyToMany
  @JoinTable(
    name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"),
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
  )
  private List<Role> roles;

  // Este atributo NO es un campo de la BBDD y no se va a persistir.
  // Es solo una bandera que se usa en la clase.
  // Esto se indica con @Transient.
  @Transient
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private boolean admin;

  // Con primitivo boolean, el valor por defecto es 0.
  //
  // Para evitar el valor 0: 
  // 1. Se puede indicar que por defecto el valor es 1, en un constructor por ejemplo.
  // 2. Usar @PrePersist e indicar ahí que el valor sea 1. Es la opción que se ha tomado.
  private boolean enabled;

  public User() {
    roles = new ArrayList<>();
  }

  @PrePersist
  public void prePersist() {
    enabled = true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  // Por si quisiéramos eliminar algún objeto relacionado.
  // Dado un usuario, eliminar roles.
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }
}
