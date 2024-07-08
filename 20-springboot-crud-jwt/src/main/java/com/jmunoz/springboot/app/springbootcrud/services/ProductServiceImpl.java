package com.jmunoz.springboot.app.springbootcrud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.app.springbootcrud.entities.Product;
import com.jmunoz.springboot.app.springbootcrud.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

  private ProductRepository repository;

  ProductServiceImpl() {
  }

  @Autowired
  ProductServiceImpl(ProductRepository repository) {
    this.repository = repository;
  };

  @Override
  @Transactional(readOnly = true)
  public List<Product> findAll() {
    return (List<Product>) repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Product> findById(Long id) {
    return repository.findById(id); 
  }

  @Override
  @Transactional
  public Product save(Product product) {
    return repository.save(product);
  }

  @Override
  @Transactional
  public @Nullable Optional<Product> update(Long id, Product product) {
    Optional<Product> productOptional = repository.findById(id);
    if (productOptional.isPresent()) {
      Product productBd = productOptional.orElseThrow();

      productBd.setName(product.getName());
      productBd.setDescription(product.getDescription());
      productBd.setPrice(product.getPrice());
      productBd.setSku(product.getSku());
      return Optional.of(repository.save(productBd));
    }

    return productOptional;
  }

  @Override
  @Transactional
  public @Nullable Optional<Product> delete(Long id) {
    Optional<Product> productOptional = repository.findById(id);
    productOptional.ifPresent(productBd -> {
      repository.delete(productBd);
    });

    return productOptional;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsBySku(String sku) {
    return repository.existsBySku(sku);
  }
}
