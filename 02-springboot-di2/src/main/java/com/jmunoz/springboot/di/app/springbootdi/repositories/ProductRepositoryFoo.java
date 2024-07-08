package com.jmunoz.springboot.di.app.springbootdi.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.jmunoz.springboot.di.app.springbootdi.models.Product;

@Repository
@Primary
public class ProductRepositoryFoo implements ProductRepository {

  @Override
  public List<Product> findAll() {
    return Collections.singletonList(new Product(1L, "Monitor Asus 27", 600L));
  }

  @Override
  public Product findById(Long id) {
    return new Product(id, "Monitor Asus 27", 600L);
  }
  
}
