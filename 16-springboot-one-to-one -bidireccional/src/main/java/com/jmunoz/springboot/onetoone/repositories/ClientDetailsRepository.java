package com.jmunoz.springboot.onetoone.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.onetoone.entities.ClientDetails;

public interface ClientDetailsRepository extends CrudRepository<ClientDetails, Long> {
  
}
