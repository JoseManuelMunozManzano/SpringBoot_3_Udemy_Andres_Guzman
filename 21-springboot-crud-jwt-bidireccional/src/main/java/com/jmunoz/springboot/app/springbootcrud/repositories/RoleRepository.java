package com.jmunoz.springboot.app.springbootcrud.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.app.springbootcrud.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

  // Método con consulta basada en nombre (atributo name)
  Optional<Role> findByName(String name);
}
