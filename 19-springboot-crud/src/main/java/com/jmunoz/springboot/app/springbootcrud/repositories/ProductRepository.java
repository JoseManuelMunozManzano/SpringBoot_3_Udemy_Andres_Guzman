package com.jmunoz.springboot.app.springbootcrud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.app.springbootcrud.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
  
  // Método basado en el nombre para saber si existe el campo sku
  // Convención por sobre configuración.
  boolean existsBySku(String sku);
}
