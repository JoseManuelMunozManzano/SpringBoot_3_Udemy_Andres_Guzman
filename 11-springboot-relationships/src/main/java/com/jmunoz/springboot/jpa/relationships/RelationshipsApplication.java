package com.jmunoz.springboot.jpa.relationships;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.jpa.relationships.entities.Address;
import com.jmunoz.springboot.jpa.relationships.entities.Client;
import com.jmunoz.springboot.jpa.relationships.entities.Invoice;
import com.jmunoz.springboot.jpa.relationships.repositories.ClientRepository;
import com.jmunoz.springboot.jpa.relationships.repositories.InvoiceRepository;

@SpringBootApplication
public class RelationshipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelationshipsApplication.class, args);
	}

  @Bean
  CommandLineRunner commandLineRunner(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {

		return runner -> {
			// manyToOne(clientRepository, invoiceRepository);
			// manyToOneFindByIdClient(clientRepository, invoiceRepository);
			oneToMany(clientRepository);
		};
	}

	@Transactional
	private void oneToMany(ClientRepository clientRepository) {
		Client client = new Client("Frank", "Moras");

		Address address1 = new Address("El Verjel", 1234);
		Address address2 = new Address("Vasco de Gama", 9875);

		// Asignamos direcciones al cliente
		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		// De forma automática guarda las direcciones, gracias al cascade
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	private void manyToOne(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {

		Client client = new Client("John", "Doe");
		clientRepository.save(client);

		// Una vez creado el cliente, guardamos la relación en el objeto invoice.
		// Luego ya podemos guardar la factura.
		Invoice invoice = new Invoice("Compras de oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceDB = invoiceRepository.save(invoice);

		// invoiceDB contiene tanto la factura como el cliente.
		System.out.println(invoiceDB);
	}

	@Transactional
	private void manyToOneFindByIdClient(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {

		Optional<Client> optionalClient = clientRepository.findById(1L);

		if (optionalClient.isPresent()) {
			Client client = optionalClient.orElseThrow();
			Invoice invoice = new Invoice("Compras de oficina", 2000L);
			invoice.setClient(client);
			Invoice invoiceDB = invoiceRepository.save(invoice);
			System.out.println(invoiceDB);
		}
	}
}
