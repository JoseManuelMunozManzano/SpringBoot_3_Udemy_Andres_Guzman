package com.jmunoz.springboot.onetoone.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.onetoone.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

  @Query("select c from Client c left join fetch c.clientDetails where c.id = :id")
  Optional<Client> findOne(Long id);
}
