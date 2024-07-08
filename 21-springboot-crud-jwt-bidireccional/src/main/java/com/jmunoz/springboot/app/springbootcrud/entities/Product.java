package com.jmunoz.springboot.app.springbootcrud.entities;

import com.jmunoz.springboot.app.springbootcrud.validation.IsExistsBD;
import com.jmunoz.springboot.app.springbootcrud.validation.IsRequired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // @NotNull solo vale para objetos o tipos wrapper como Integer, pero no vale para String, por eso usamos @NotEmpty
  // @NotBlank sería como @NotEmpty, pero además valida que no sea un texto vacío.
  @NotEmpty(message = "{NotEmpty.product.name}")
  @Size(min = 3, max = 30)
  private String name;

  @NotNull(message = "{NotNull.product.price}")
  @Min(value = 500, message = "{Min.product.price}")
  private Integer price;

  // Comentamos porque usamos nuestra anotación personalizada.
  // @NotBlank(message = "{NotBlank.product.description}")
  //
  // Usando nuestra anotación.
  // @IsRequired
  //
  // Usando nuestra anotación, pero volviendo a cambiar el mensaje usando messages.properties
  @IsRequired(message = "{IsRequired.product.description}")
  private String description;

  @IsExistsBD
  @IsRequired
  private String sku;

  // Por ahora no hace falta constructor porque todos los datos los pasamos mediante API REST.
  // Tampoco hace falta el toString()

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
  public Integer getPrice() {
    return price;
  }
  public void setPrice(Integer price) {
    this.price = price;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getSku() {
    return sku;
  }
  public void setSku(String sku) {
    this.sku = sku;
  }
}
