package com.jmunoz.springboot.onetoone.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  // Relación OneToOne bidireccional con el detalle del cliente.
  // Cuando es @xxxToOne por defecto es EAGER. En este caso sería EAGER pero la hemos cambiado a LAZY.
  // Cuando es @xxxToMany (variables List o Set) por defecto es LAZY.
  // La clase ClientDetails es la dueña de la relación, pero la clase principal (o padre) es Cliente.
  // Aquí es donde vamos a tener el Cascade, el orphanRemoval... para que, cuando creemos el cliente
  // se cree también su detalle.
  // Recordar que en mappedBy se indica el nombre del atributo de tipo Client indicado en ClientDetails.
  //
  // Para resumir, donde vaya el mappedBy es la clase principal o padre.
  // Donde va el @JoinColumn es la clase hija o dependiente, pero que es la dueña de 
  // la relación, donde aparecerá el campo nuevo.
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
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

    // Hacemos aquí la relación inversa.
    clientDetails.setClient(this);
  }

  public void removeClientDetails(ClientDetails clientDetails) {
    // Primero este porque si indicamos que this es null, su relación inversa no vamos a poder hacerla.
    clientDetails.setClient(null);

    // Al eliminar el cliente, en cascada se elimina el detalle.
    // Como tenemos el orphanRemoval, se elimina el registro y no va a quedar huérfano.
    this.clientDetails = null;
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
