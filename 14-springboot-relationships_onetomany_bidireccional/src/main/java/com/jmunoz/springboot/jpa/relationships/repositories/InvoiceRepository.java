package com.jmunoz.springboot.jpa.relationships.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.jpa.relationships.entities.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
  
}
