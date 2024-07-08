package com.jmunoz.springboot.onetoone.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "clients_details")
public class ClientDetails {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private boolean isPremium;
  private Integer points;

  // Relación OneToOne bidireccional con el cliente.
  // Nombre por defecto del campo: client_id, pero indicamos en la @JoinColumn que se va a llamar id_cliente
  // Tenemos que indicar quién es el dueño de la relación. Como queremos que la foreign key
  // esté en esta tabla, client_details será la dueña de la relación.
  // Recordar que donde pongamos la @JoinColumn es donde va la foreign key.
  // Esta tabla también es hija de Client.
  @OneToOne
  @JoinColumn(name = "id_cliente")
  private Client client;

  
  public ClientDetails() {
  }

  public ClientDetails(boolean isPremium, Integer points) {
    this.isPremium = isPremium;
    this.points = points;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public boolean isPremium() {
    return isPremium;
  }
  public void setPremium(boolean isPremium) {
    this.isPremium = isPremium;
  }
  public Integer getPoints() {
    return points;
  }
  public void setPoints(Integer points) {
    this.points = points;
  }
  public Client getClient() {
    return client;
  }
  public void setClient(Client client) {
    this.client = client;
  }

  // Para evitar dependencias circulares NO podemos añadir aquí la referencia a Client.
  // Client SI tiene en su toString una referencia a ClientDetails.
  @Override
  public String toString() {
    return "{id=" + id +
           ", isPremium=" + isPremium +
           ", points=" + points +
           "}";
  }
}
