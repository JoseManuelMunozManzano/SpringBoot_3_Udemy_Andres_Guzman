package com.jmunoz.springboot.jpa.relationships.entities;

import java.util.HashSet;
import java.util.Set;

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

  // SOLUCION 1 AL PROBLEMA DE NO PERMITIR MAS DE 1 LEFT JOIN 
  // En vez de List, obtener un Set, tanto aquí como en invoices
  // PROBLEMA GORDO: No es bueno. Se hace producto cartesiano.. No recomendado.
  // SOLUCION 2: https://stackoverflow.com/questions/30088649/how-to-use-multiple-join-fetch-in-one-jpql-query
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "tbl_clientes_to_direcciones", 
    joinColumns = @JoinColumn(name = "id_cliente"), 
    inverseJoinColumns = @JoinColumn(name = "id_direcciones"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_direcciones"}))
  private Set<Address> addresses;

  // Relación bidireccional.
  // Un cliente puede tener muchas facturas y una factura pertenece a un cliente.
  //
  // ¿Quién va a ser el dueño de la relación? ¿Dónde va a estar la foreign key? El dueño de la relación es Invoice,
  // que es donde hemos indicado el @JoinColumn.
  // No podemos tener aquí @JoinColumn porque no podemos crear el campo de la relación en ambas tablas.
  //
  // Aquí tenemos que indicar el mapeado inverso, mappedBy, siendo el valor el nombre del atributo de la contraparte.
  // En este ejemplo, en Invoice.java el campo se llama client, así que aquí indicamos client.
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
  private Set<Invoice> invoices;

  public Client() {
    invoices = new HashSet<>();
    addresses = new HashSet<>();
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
  public Set<Invoice> getInvoices() {
    return invoices;
  }
  public Set<Address> getAddresses() {
    return addresses;
  }
  public void setAddresses(Set<Address> addresses) {
    this.addresses = addresses;
  }
  public void setInvoices(Set<Invoice> invoices) {
    this.invoices = invoices;
  }
  public Client addInvoice(Invoice invoice) {
    invoices.add(invoice);
   	// Y, como es una relación bidireccional, a cada factura tenemos que indicarle el cliente.
  	invoice.setClient(this);
    return this;
  }

  public void removeInvoice(Invoice invoice) {
    // Implementamos hashCode y equals en la clase Invoice.java para que compare 
    // por id, descripción y total, y no por referencia de la instancia.
    // NOTA: hashCode se aplica a los Set y equals a los List, pero hay que tener ambos.
    this.getInvoices().remove(invoice);

    // Para la relación inversa.
    // Desde el objeto factura eliminamos el cliente.
    invoice.setClient(null);
  }

  // No podemos poner en el toString de ambas clases la relación de la otra por un tema de ciclo infinito (circular)
  // Aqui mostramos los invoices.
  @Override
  public String toString() {
    return "{id=" + id + 
           ", name=" + name + 
           ", lastname=" + lastname +
           ", addresses=" + addresses +
           ". invoices=" + invoices +
           "}";
  }
}
