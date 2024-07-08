package com.jmunoz.springboot.onetoone.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String lastname;

  // Relación OneToOne unidireccional con el detalle del cliente.
  // Nombre por defecto del campo: client_details_id, pero lo cambiamos por id_cliente_detalle
  // Cuando es @xxxToOne por defecto es EAGER. En este caso es EAGER.
  // Cuando es @xxxToMany (variables List o Set) por defecto es LAZY.
  @OneToOne
  @JoinColumn(name = "id_cliente_detalle")
  private ClientDetails clientDetails;
  
  public Client() {
  }

  public Client(String name, String lastname) {
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
  public ClientDetails getClientDetails() {
    return clientDetails;
  }
  public void setClientDetails(ClientDetails clientDetails) {
    this.clientDetails = clientDetails;
  }

  @Override
  public String toString() {
    return "{id=" + id +
           ", name=" + name +
           ", lastname=" + lastname +
           ", clientDetails=" + clientDetails +
           "}";
  }
}
