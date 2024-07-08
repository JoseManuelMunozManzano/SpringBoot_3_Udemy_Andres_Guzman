package com.jmunoz.curso.springboot.di.factura.springbootdifactura;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.jmunoz.curso.springboot.di.factura.springbootdifactura.models.Item;
import com.jmunoz.curso.springboot.di.factura.springbootdifactura.models.Product;

@Configuration
@PropertySource(value = "classpath:data.properties", encoding = "UTF-8")
public class AppConfig {

  @Bean
  @Primary
  List<Item> itemsInvoice() {

    Product p1 = new Product("Cámara Sony", 800);
    Product p2 = new Product("Bicicleta Bianchi 26", 1200);
    
    return Arrays.asList(new Item(p1, 2), 
                         new Item(p2, 4));
  }
  
  @Bean("office")
  List<Item> itemsInvoiceOffice() {

    return Arrays.asList(new Item(new Product("Monitor Asus 24", 700), 4), 
                         new Item(new Product("Notebook Razer", 1400), 6),
                         new Item(new Product("Impresora HP", 800), 1),
                         new Item(new Product("Escritorio Oficina", 900), 4));
  }  
}
