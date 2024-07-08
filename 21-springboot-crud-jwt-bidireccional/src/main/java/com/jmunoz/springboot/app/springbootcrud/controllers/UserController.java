package com.jmunoz.springboot.app.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmunoz.springboot.app.springbootcrud.entities.User;
import com.jmunoz.springboot.app.springbootcrud.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
public class UserController {

  private UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping
  public List<User> list() {
    return service.findAll();
  }

  // Como estamos utilizando validación, usamos ResponseEntity<?>, porque a veces 
  // devolvemos User y otras veces un Map con errores.
  // Este método se queda privado, para usar por usuarios autenticados.
  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {

    if (result.hasFieldErrors()) {
      return validation(result);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
  }

  // Este es el método (en vez de create) que queda público para poder dar de alta un usuario
  // con rol ROLE_USER
  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
    // Nos aseguramos de que no se da de alta un rol ROLE_ADMIN.
    user.setAdmin(false);
    return create(user, result);
  }

  // Devolvemos un JSON de error personalizado.
  private ResponseEntity<?> validation(BindingResult result) {

    Map<String, String> errors = new HashMap<>();

    result.getFieldErrors().forEach(err -> {
      errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });

    return ResponseEntity.badRequest().body(errors);
  }
}
