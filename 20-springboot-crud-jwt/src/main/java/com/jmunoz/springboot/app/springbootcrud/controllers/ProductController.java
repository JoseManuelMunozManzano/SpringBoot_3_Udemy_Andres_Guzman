package com.jmunoz.springboot.app.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// import com.jmunoz.springboot.app.springbootcrud.ProductValidation;
import com.jmunoz.springboot.app.springbootcrud.entities.Product;
import com.jmunoz.springboot.app.springbootcrud.services.ProductService;
import com.jmunoz.springboot.app.springbootcrud.validation.groups.OnCreate;
import com.jmunoz.springboot.app.springbootcrud.validation.groups.OnUpdate;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private ProductService service;
  
  // No hace falta si queremos hacer la validación por Properties
  //
  // @Autowired
  // private ProductValidation validation;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public List<Product> list() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<?> view(@PathVariable Long id) {
    Optional<Product> productOptional = service.findById(id);
    if (productOptional.isPresent()) {
      return ResponseEntity.ok(productOptional.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  // Mapeo automático.
  // Gracias a Jackson, se convierte el JSON del @RequestBody a un objeto de tipo Product.
  //
  // @BindingResult encapsula y tiene toda la información de los mensajes de error de las validaciones.
  // No se puede indicar el parámetro en cualquier sitio, tiene que ir después del @Valid o @Validated.
  //
  // Aquí indicamos @Validates(onCreate.class) para que solo se validen los campos que tengan el OnCreate.class en los campos
  // del entity que queramos validar al crear.
  // Notar que las clases OnCreate() y OnUpdate() son interfaces que he creado en /validation/groups
  // https://blog.stackademic.com/mastering-data-validation-with-valid-and-validated-annotations-in-spring-b9bb1750de25
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @CrossOrigin(origins = {"http://myappfrontend:4200", "http://otraruta:3300"}, originPatterns = "*")
  public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody Product product, BindingResult result) {

    // Si queremos hacer la validación programática.
    // No hace falta si queremos hacer la validación por Properties.
    //
    // validation.validate(product, result);

    if (result.hasFieldErrors()) {
      return validation(result);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> update(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody Product product, BindingResult result) {

    // Si queremos hacer la validación programática.
    // No hace falta si queremos hacer la validación por Properties.
    //
    // validation.validate(product, result);

    if (result.hasFieldErrors()) {
      return validation(result);
    }

    Optional<Product> productOptional = service.update(id, product);
    if (productOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    Optional<Product> productOptional = service.delete(id);
    if (productOptional.isPresent()) {
      return ResponseEntity.ok(productOptional.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  // Devolvemos un JSON de error personalizado.
  private ResponseEntity<?> validation(BindingResult result) {

    // Guardamos los mensajes de error de cada campo.
    // A partir de este Map se genera un JSON del tipo "atributo": "valor"
    Map<String, String> errors = new HashMap<>();

    result.getFieldErrors().forEach(err -> {
      errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });

    // Cuando no pasa la validación siempre se devuelve un Bad Request.
    return ResponseEntity.badRequest().body(errors);
  }
}
