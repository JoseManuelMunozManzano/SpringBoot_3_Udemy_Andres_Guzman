package com.jmunoz.curso.springboot.di.factura.springbootdifactura.models;

public class Item {
  
  private Product product;
  private Integer quantity;

  public Item() {
  }

  public Item(Product product, Integer quantity) {
    this.product = product;
    this.quantity = quantity;
  }
  
  public Product getProduct() {
    return product;
  }
  public void setProduct(Product product) {
    this.product = product;
  }
  public Integer getQuantity() {
    return quantity;
  }
  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  // Los métodos get se añaden automáticamente al JSON devuelto
  public int getImporte() {
    return quantity * product.getPrice();
  }
}
