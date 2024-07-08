package com.jmunoz.springboot.error.springbooterror.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jmunoz.springboot.error.springbooterror.exceptions.UserNotFoundException;
import com.jmunoz.springboot.error.springbooterror.models.domain.User;
import com.jmunoz.springboot.error.springbooterror.services.UserService;

@RestController
public class AppController {

  @Autowired
  private UserService service;
  
  @GetMapping("/app")
  public String index() {

    @SuppressWarnings("unused")
    int value = 100 / 0;

    // No nos interesa lo que devuelve, sino como tratar las excepciones, devolviendo un JSON de error amigable.
    return "ok 200";
  }

  @GetMapping("/app2")
  public String index2() {

    @SuppressWarnings("unused")
    int value = Integer.parseInt("10x");

    // No nos interesa lo que devuelve, sino como tratar las excepciones, devolviendo un JSON de error amigable.
    return "ok 200";
  }

  @GetMapping("/show/{id}")
  public User show(@PathVariable(name = "id") Long id) {

    User user = service.findById(id);
    
    // Provocamos la excepción cuando findById devuelve null.
    // Esto provoca una excepción con un id no existente (por ejemplo 1000)
    //
    // NOTA: Hay programadores que prefieren manejar las excepciones en el controller y no en el service, y al revés.
    if (user == null) {
      throw new UserNotFoundException("Error. El usuario no existe!");
    }
    System.out.println(user.getLastname());

    return user;
  }

  @GetMapping("/show2/{id}")
  public ResponseEntity<?> show2(@PathVariable(name = "id") Long id) {

    // Lanzando una excepción personalizada usando Optionals y orElseThrow
    User user = service.findByIdOptional(id).orElseThrow(() -> new UserNotFoundException("Error. El usuario (usando Optional) no existe!"));

    return ResponseEntity.ok(user);

    // Otra forma, pero no va a HandlerExceptionController.java, es decir, el body queda vacío
    //
    // Optional<User> optionalUser = service.findByIdOptional(id);
    // if (optionalUser.isEmpty()) {
    //   return ResponseEntity.notFound().build();
    // }
    //
    // return ResponseEntity.ok(optionalUser.get());
  }
}
