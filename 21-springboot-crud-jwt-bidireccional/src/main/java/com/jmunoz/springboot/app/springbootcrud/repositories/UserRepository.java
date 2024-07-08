package com.jmunoz.springboot.app.springbootcrud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.app.springbootcrud.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
  
}
