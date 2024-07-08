package com.jmunoz.springboot.jpa.relationships.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.jpa.relationships.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

  // Indicamos left join para que recupere cuando NO haya direcciones.
  @Query("select c from Client c left join fetch c.addresses where c.id=:id")
  Optional<Client> findOneWithAddresses(Long id);

  // Indicamos left join para que recupere cuando NO hay facturas.
  @Query("select c from Client c left join fetch c.invoices where c.id=:id")
  Optional<Client> findOneWithInvoices(Long id);

  // Indicamos left join para que recupere cuando NO hay facturas y/o no haya direcciones.
  // Esto falla porque no se permiten 2 join
  @Query("select c from Client c left join fetch c.invoices left join fetch c.addresses where c.id=:id")
  Optional<Client> findOne(Long id);
}
