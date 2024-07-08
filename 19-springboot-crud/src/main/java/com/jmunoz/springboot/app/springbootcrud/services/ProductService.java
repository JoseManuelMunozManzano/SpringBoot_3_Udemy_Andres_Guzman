package com.jmunoz.springboot.app.springbootcrud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.Nullable;

import com.jmunoz.springboot.app.springbootcrud.entities.Product;

public interface ProductService {

  List<Product> findAll();

  Optional<Product> findById(Long id);

  Product save(Product product);

  @Nullable Optional<Product> update(Long id, Product product);

  @Nullable Optional<Product> delete(Long id);

  boolean existsBySku(String sku);
}
