package com.jmunoz.springboot.di.app.springbootdi.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jmunoz.springboot.di.app.springbootdi.models.Product;
import com.jmunoz.springboot.di.app.springbootdi.repositories.ProductRepository;

// @Component
@Service
public class ProductServiceImpl implements ProductService {
  
  // Comentado porque se inyecta automáticamente en el constructor
  // Un ejemplo de @Qualifier en el atributo
  // @Autowired
  // @Qualifier("productList")
  private ProductRepository repository;

  // Usando @Value
  //
  // @Value("${config.tax.value}")
  // private double taxValue;

  // Si se pasa por constructor quitamos este @Autowired
  // @Autowired
  private Environment environment;

// No hace falta indicar @Autowired si solo hay un constructor y el parámetro es un componente de Spring
// Inyectamos, usando @Qualifier
//     Cambiar entre productJson y productList para obtener distintos valores
//     Quitar todo el @Qualifier para usar ProductRepositoryFoo
// Inyectamos Environment
  public ProductServiceImpl(@Qualifier("productJson") ProductRepository repository, Environment environment) {
    this.repository = repository;
    this.environment = environment;
  }

  // @Autowired
  // public void setRepository(ProductRepository repository) {
  //   this.repository = repository;
  // }

  @Override
  public List<Product> findAll() {

    return repository.findAll().stream().map(p -> {
      double tax = Optional.of(environment.getProperty("config.tax.value", Double.class)).orElse(1d);
      Double priceWithTax = p.getPrice() * tax;

      // Usando @Value
      //
      // Double priceWithTax = p.getPrice() * taxValue;

      // DESCOMENTAR ESTO PARA QUE FUNCIONE CORRECTAMENTE EN UN SCOPE SINGLETON
      // (Y PORQUE ES BUENA PRACTICA QUE SEA INMUTABLE)
      //
      Product newProduct = (Product) p.clone();
      newProduct.setPrice(priceWithTax.longValue());
      return newProduct;

      // COSA QUE NO DEBEMOS HACER: Vamos a mutar el objeto original
      // Lo hacemos para las pruebas del contexto @RequestScope y del contexto @SessionScope
      // indicado en ProductRepositoryImpl.java.
      // Contexto @RequestScope
      //   Consigue que sea inmutable para efectos de la aplicación completa
      //   Se puede probar con Postman
      // Contexto @SessionScope
      //   Mutable durante una sesión. Si se cierra y se abre otra sesión los datos se inicializan
      //   Probar con Postman y a la vez en el navegador. Los valores que aparecen en uno y otro acaban siendo distintos
      //   También se puede probar en dos navegadores distintos a la vez
      // COMENTAR ESTO PARA QUE FUNCIONE CORRECTAMENTE EN SCOPE SINGLETON
      //
      // p.setPrice(priceWithTax.longValue());
      // return p;
    }).collect(Collectors.toList());
  }

  @Override
  public Product findById(Long id) {

    return repository.findById(id);
  }
}
