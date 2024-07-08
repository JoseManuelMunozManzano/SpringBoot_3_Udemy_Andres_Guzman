package com.jmunoz.curso.springboot.di.factura.springbootdifactura.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmunoz.curso.springboot.di.factura.springbootdifactura.models.Client;
import com.jmunoz.curso.springboot.di.factura.springbootdifactura.models.Invoice;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
  
  @Autowired
  private Invoice invoice;

  @GetMapping("/show")
  public Invoice show() {
    // Alternativa a @JsonIgnoreProperties.
    // Crear una instancia nueva y pasar solo los valores que necesitamos.
    Invoice i = new Invoice();
    Client c = new Client();

    c.setName(invoice.getClient().getName());
    c.setLastname(invoice.getClient().getLastname());
    
    i.setClient(invoice.getClient());
    i.setDescription(invoice.getDescription());
    i.setItems(invoice.getItems());

    return i;

    // return invoice;
  }
}
