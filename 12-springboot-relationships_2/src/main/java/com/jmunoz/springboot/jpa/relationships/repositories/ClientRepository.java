package com.jmunoz.springboot.jpa.relationships.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.jpa.relationships.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
  
}
