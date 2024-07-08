package com.jmunoz.springboot.app.springbootcrud.services;

import java.util.List;

import com.jmunoz.springboot.app.springbootcrud.entities.User;

public interface UserService {

  // Podría devolver un DTO, pero como lo queremos devolver todo, usaremos la clase Entity.
  // Si no quisiera devolver los roles, o reducir el JSON que devolvemos, si que me haría falta una clase intermedia DTO.
  List<User> findAll();

  User save(User user);

  boolean existsByUsername(String username);
}
