package com.jmunoz.curso.springboot.di.factura.springbootdifactura.models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

// Cuidado, al indicar @RequestScope, el proxy genera atributos residuales que tenemos que omitir en la construcción del JSON.
// Se usa la anotación @JsonIgnoreProperties para ignorar esos atributos residuales.
// Se comenta porque se usa la alternativa en el controlador, método show()
@Component
@RequestScope
// @JsonIgnoreProperties({"targetSource", "advisors"})
public class Invoice {
  
  @Autowired
  private Client client;

  @Value("${invoice.description.office}")
  private String description;

  @Autowired
  @Qualifier("office")
  private List<Item> items;

  public Invoice() {
    System.out.println("Creando el componente de la factura");
    // Atributos sin valores inyectados. Valen null.
    System.out.println(client);
    System.out.println(description);
  }

  @PostConstruct
  public void init() {
    System.out.println("Creando el componente de la factura");
    // Atributos con valores inyectados. Se puede manipular la información.
    // Como ahora tenemos scope de request, pero client es singleton, por cada petición se va añadiendo Pepe a lo que ya hay.
    client.setName(client.getName().concat(" Pepe"));
    description = description.concat(" del cliente: ").concat(client.getName()).concat(" ").concat(client.getLastname());
  }

  @PreDestroy
  public void destroy() {
    System.out.println("Destruyendo el componente o bean invoice!");
  }

  public Client getClient() {
    return client;
  }
  public void setClient(Client client) {
    this.client = client;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public List<Item> getItems() {
    return items;
  }
  public void setItems(List<Item> items) {
    this.items = items;
  }

  // El JSON se genera mediante los métodos get.
  // Con los atributos no porque son privados.
  public int getTotal() {
    // int total = 0;
    // for (Item item : items) {
    //   total += item.getImporte();
    // }
    // return total;

    // Usando streams
    //
    // Para poder usar el reduce, tanto sum como item tienen que ser enteros.
    // Como item es un objeto, antes le pasamos un map para devolver un entero (importe).
    // En el reduce indicamos un valor inicial y un callback.
    return items.stream()
    .map(item -> item.getImporte())
    .reduce(0, (sum, importe) -> sum + importe);
  }
}
