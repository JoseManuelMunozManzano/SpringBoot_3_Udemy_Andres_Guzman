package com.jmunoz.springboot.jpa.relationships.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoices")
public class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;
  private Long total;

  // Un cliente puede tener muchas facturas y una factura pertenece a un cliente.
  // En este caso la relación es @ManyToOne ya que: Muchas facturas -> Un cliente
  // El primer elemento de la cardinalidad se refiere a la clase donde está el atributo, es decir,
  // el @Many se refiere a Invoice.
  // En BBDD se va a crear una foreign key en la tabla invoices que apuntará al id de la tabla clients.
  // La tabla invoices va a ser la dueña de la relación porque contiene la llave foranea
  // hacia el cliente.
  // El nombre del campo se genera con el nombre del atributo, seguido de guión bajo y id: client_id
  // Este es el standard, usando la property spring.jpa.hibernate.ddl-auto, pero puede 
  // personalizarse el nombre de nuestra llave foranea usando la anotación @JoinColumn
  // Se indica aquí un ejemplo de @JoinColumn, pero como el nombre indicado sería el por defecto
  // no haría falta.
  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  public Invoice() {
  }

  public Invoice(String description, Long total) {
    this.description = description;
    this.total = total;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Long getTotal() {
    return total;
  }
  public void setTotal(Long total) {
    this.total = total;
  }
  public Client getClient() {
    return client;
  }
  public void setClient(Client client) {
    this.client = client;
  }
  
  @Override
  public String toString() {
    return "{id=" + id + ", description=" + description + ", total=" + total + ", client=" + client + "}";
  }
}
