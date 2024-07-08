package com.jmunoz.springboot.jpa.relationships.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "clients")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String lastname;

  // Un cliente puede tener muchas direcciones.
  //
  // Como hemos indicado @JoinTable, NO se crea tabla intermedia, sino que usa la tabla intermedia
  // indicada.
  // Realmente, mientras tengamos en el properties ddl-auto=create, la va a crear, pero sin esa property
  // la tabla tendría que existir.
  //
  // Ahora indicamos las foreign keys, que son dos.
  // Usando joinColumns, indicamos que campo contendrá el id de cliente (el que se puede repetir)
  // y que es dueña de la relación. En el ejemplo el campo se llama id_cliente
  // Y luego indicamos el campo de la relación inversa, el de las direcciones, que no pueden repetirse, usando
  // inverseJoinColumns, llamada en el ejemplo id_direcciones
  // Para indicar que no puede repetirse se usa uniqueConstraints, y la anotación
  // @UniqueConstraints donde indicaremos las columnas que son únicas.
  //
  // Tenemos que inicializar la lista, ya sea en la declaración o en el constructor.
  //
  // Con cascade indicamos que, cuando se cree un cliente, en cascada cree también
  // sus direcciones.
  // O si eliminamos un cliente, se elimina en cascada su relación.
  //
  // Para borrar los datos de la tabla addresses cuando se elimina la relación tbl_clientes_to_direcciones
  // Si no indicamos esta propiedad, en la tabla addresses el registro queda huérfano.
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "tbl_clientes_to_direcciones", 
    joinColumns = @JoinColumn(name = "id_cliente"), 
    inverseJoinColumns = @JoinColumn(name = "id_direcciones"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_direcciones"}))
  private List<Address> addresses;

  public Client() {
    addresses = new ArrayList<>();
  }

  public Client(String name, String lastname) {
    this();
    this.name = name;
    this.lastname = lastname;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getLastname() {
    return lastname;
  }
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
  public List<Address> getAddresses() {
    return addresses;
  }
  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  @Override
  public String toString() {
    return "{id=" + id + ", name=" + name + ", lastname=" + lastname + ", addresses=" + addresses + "}";
  }
}
