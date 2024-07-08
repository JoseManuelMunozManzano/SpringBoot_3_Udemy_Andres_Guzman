package com.jmunoz.springboot.jpa.relationships.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.jpa.relationships.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
  
  // En vez del property spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true,
  // lo mejor para evitar errores por cierre de conexión es hacer consultas personalizadas.
  // Indicamos left join para que recupere datos aunque no haya direcciones.
  @Query("select c from Client c left join fetch c.addresses where c.id = :id")
  Optional<Client> findOne(Long id);
}
