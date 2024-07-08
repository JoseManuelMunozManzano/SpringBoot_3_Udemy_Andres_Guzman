package com.jmunoz.springboot.di.app.springbootdi.services;

import java.util.List;
import java.util.stream.Collectors;

import com.jmunoz.springboot.di.app.springbootdi.models.Product;
import com.jmunoz.springboot.di.app.springbootdi.repositories.ProductRepositoryImpl;

public class ProductServiceImpl implements ProductService {
  
  private ProductRepositoryImpl repository = new ProductRepositoryImpl();

  // En vez de devolver directamente Product, también podríamos haber mapeado Product a un DTO 
  // y devolver este, con los campos que realmente necesito y la data ya formateada.
  @Override
  public List<Product> findAll() {

    // El map respeta la inmutabilidad, porque genera un nuevo producto.
    return repository.findAll().stream().map(p -> {
      Double priceWithTax = p.getPrice() * 1.25d;
      // Pero aquí no respetamos la inmutabilidad, ya que estamos modificando
      // la data del producto original. En concreto, devolvemos un nuevo producto
      // pero también modificamos el que estamos tratando.
      // Esto ocurre porque la data está en memoria. Con data que venga de BBDD este
      // problema no ocurriría, pero seguiría siendo una mala práctica.
      //
      // p.setPrice(priceWithTax.longValue());    <--- Mala práctica!!!
      //
      // Esto lo tenemos que arreglar.
      //
      // Forma 1. En vez de modificar el producto original, tenemos que crear una nueva 
      // instancia de Producto (o clonarla) y devolverla.
      //
      // Product newProduct = new Product(p.getId(), p.getName(), priceWithTax.longValue());
      //
      // Forma 2. Añadiendo al modelo Product.java el implements Cloneable y sobreescribiendo 
      // el método clone(), se puede usar, junto un casting, de la siguiente forma:
      Product newProduct = (Product) p.clone();
      newProduct.setPrice(priceWithTax.longValue());

      return newProduct;
    }).collect(Collectors.toList());
  }

  @Override
  public Product findById(Long id) {

    return repository.findById(id);
  }
}
