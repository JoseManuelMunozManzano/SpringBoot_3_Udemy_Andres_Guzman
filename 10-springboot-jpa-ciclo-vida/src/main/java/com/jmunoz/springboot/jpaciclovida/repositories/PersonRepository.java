package com.jmunoz.springboot.jpaciclovida.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.jpaciclovida.entities.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

}
