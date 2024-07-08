package com.jmunoz.springboot.di.app.springbootdi.repositories;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.springboot.di.app.springbootdi.models.Product;

public class ProductRepositoryJson implements ProductRepository {

  private List<Product> list;

  public ProductRepositoryJson() {
    Resource resource = new ClassPathResource("json/product.json");
    readValueJson(resource);
  }

  // Este constructor se ha hecho para probar como funciona la inyección de un @Value desde la clase AppConfig
  public ProductRepositoryJson(Resource resource) {
    readValueJson(resource);
  }

  private void readValueJson(Resource resource) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Funciona tanto con getFile() como con getInputStream()
      // list = Arrays.asList(objectMapper.readValue(resource.getFile(), Product[].class));
      list = Arrays.asList(objectMapper.readValue(resource.getInputStream(), Product[].class));      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Product> findAll() {
    return list;
  }

  @Override
  public Product findById(Long id) {
    return list.stream().filter(p -> p.getId().equals(id)).findFirst().orElseThrow();
  }
  
}
