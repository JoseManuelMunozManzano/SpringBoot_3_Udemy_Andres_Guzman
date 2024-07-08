package com.jmunoz.springboot.jpa.relationships.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
  // Como no hemos indicado ninguna @JoinColumn, con esta relación se crea una tabla intermedia, clients_addresses,
  // que contendrá el id de client y el id de address. Este es el comportamiento
  // por defecto de JPA.
  //
  // Tenemos que inicializar la lista, ya sea en la declaración o en el constructor.
  //
  // Con cascade indicamos que, cuando se cree un cliente, en cascada cree también
  // sus direcciones.
  // O si eliminamos un cliente, se elimina en cascada su relación.
  //
  // Para borrar los datos de la tabla donde se almacena la relación con el cliente,
  // como el id del cliente queda null, indicamos la propiedad orphanRemoval
  // para que se elimine ese registro.
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
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
