package com.jmunoz.springboot.jpa.relationships;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

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
			//  manyToOne(clientRepository, invoiceRepository);
			// oneToManyInvoiceBidireccional(clientRepository);
			// oneToManyInvoiceBidireccionalFindById(clientRepository);
			// removeInvoiceBidireccionalFindById(clientRepository, invoiceRepository);
			removeInvoiceBidireccional(clientRepository, invoiceRepository);
		};
	}

	@Transactional
	private void oneToManyInvoiceBidireccionalFindById(ClientRepository clientRepository) {
		// Con findById, se hace la consulta y se cierra el acceso a BD.
		//
		// Optional<Client> optionalClient = clientRepository.findById(1L);
		//
		// Para evitar que luego fallen las consultas siguientes, usamos la consulta personalizada
		// que ya trae todo lo que necesitamos.
		// Pero tenemos un problema porque el left join se hace sobre Invoices y no sobre Addresses,
		// por lo que las direcciones son null y falla al hacer el toString de addresses.
		//
		// Optional<Client> optionalClient = clientRepository.findOneWithInvoices(1L);
		//
		// Creamos otra query personalizada que hace left join de Invoices y de Addresses.
		// Pero esto lanza otro error: MultipleBagFetchException: cannot simultaneously fetch multiple bags
		// https://stackoverflow.com/questions/30088649/how-to-use-multiple-join-fetch-in-one-jpql-query
		// Se soluciona de la peor manera, haciendo Set lo que deberían ser List (ver Client.java)
	  Optional<Client> optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent(client -> {
			Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de oficina", 8000L);
	
			// Asignamos al cliente las facturas, usando el nuevo método creado en Client que además hace chaining.
			// Como hemos hecho antes un findById, ya se ha cerrado la conexión, así que ahora estas sentencias fallan.
			// Como se dijo en el proyecto 13-springboot-relationships_3, lo mejor para esto es realizar una
			// consulta personalizada.
			// Y, como es una relación bidireccional, a cada factura tenemos que indicarle el cliente.
			client.addInvoice(invoice1)
						.addInvoice(invoice2);
		
			// Actualizamos el cliente.
			// El cliente contiene las facturas.
			// En cascada se actualiza luego cada factura (que tiene relacionado el cliente)
			Client clientDb = clientRepository.save(client);
	
			System.out.println(clientDb);
		});
	}

	@Transactional
	private void oneToManyInvoiceBidireccional(ClientRepository clientRepository) {
		Client client = new Client("Frank", "Moras");

		Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
		Invoice invoice2 = new Invoice("Compras de oficina", 8000L);

		// Asignamos al cliente las facturas, usando el nuevo método creado en Client que además hace chaining.
		// Y, como es una relación bidireccional, a cada factura tenemos que indicarle el cliente.
		client.addInvoice(invoice1)
		      .addInvoice(invoice2);

		// Guardamos el cliente.
		// El cliente contiene las facturas.
		// En cascada se guarda luego cada factura (que tiene relacionado el cliente)
		Client clientDb = clientRepository.save(client);

		System.out.println(clientDb);
	}

	// Este debe seguir funcionando, pero obviamente no imprimirá el cliente porque lo quitamos del toString.
	@Transactional
	public void manyToOne(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {
		Client client = new Client("John", "Doe");
		clientRepository.save(client);

		Invoice invoice = new Invoice("Compras de oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceDb = invoiceRepository.save(invoice);
		System.out.println(invoiceDb);
	}

	// Eliminar elementos hijos (facturas) dependientes del cliente.
  // Como es bidireccional vamos a eliminar las facturas del cliente y el cliente de las facturas.
	@Transactional
	private void removeInvoiceBidireccionalFindById(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {

		// Transacción donde buscamos el cliente y le asignamos facturas.
	  Optional<Client> optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent(client -> {
			Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de oficina", 8000L);
	
			client.addInvoice(invoice1)
						.addInvoice(invoice2);
		
			Client clientDb = clientRepository.save(client);
	
			System.out.println(clientDb);
		});

		// Transacción donde buscamos el cliente y le elimina una factura.
		Optional<Client> optionalClientBd = clientRepository.findOne(1L);
		optionalClientBd.ifPresent(client -> {

			// Formas de indicar factura a borrar:
			// 1. Indicar factura a eliminar (necesitmos id, descripción y total por hashCode)
			Invoice invoice3 = new Invoice("Compras de la casa", 5000L);
			invoice3.setId(2L);
			Optional<Invoice> invoiceOptional = Optional.of(invoice3);
			
			// 2. Búsqueda de la factura por id
			//Optional<Invoice> invoiceOptional = invoiceRepository.findById(2L);

			invoiceOptional.ifPresent(invoice -> {
				client.removeInvoice(invoice);

				// Como el cliente tiene todas las relaciones, se hace el save por el cliente, y
				// por cascada se hace todo lo demás.
				clientRepository.save(client);

				System.out.println(client);
			});
		});
	}

// Creamos el cliente y luego lo borramos
@Transactional
	private void removeInvoiceBidireccional(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {

		// Transacción donde creamos un cliente y le asignamos facturas.
	  Optional<Client> optionalClient = Optional.of(new Client("Frank", "Moras"));

		optionalClient.ifPresent(client -> {
			Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de oficina", 8000L);
	
			client.addInvoice(invoice1)
						.addInvoice(invoice2);
		
			Client clientDb = clientRepository.save(client);
	
			System.out.println(clientDb);
		});

		// Transacción donde buscamos el cliente y le elimina una factura.
		Optional<Client> optionalClientBd = clientRepository.findOne(3L);
		optionalClientBd.ifPresent(client -> {

			// Formas de indicar factura a borrar:
			// 1. Indicar factura a eliminar (necesitmos id, descripción y total por hashCode)
			// Invoice invoice3 = new Invoice("Compras de la casa", 5000L);
			// invoice3.setId(2L);
			// Optional<Invoice> invoiceOptional = Optional.of(invoice3);
			
			// 2. Búsqueda de la factura por id
			Optional<Invoice> invoiceOptional = invoiceRepository.findById(2L);

			invoiceOptional.ifPresent(invoice -> {
				client.removeInvoice(invoice);

				// Como el cliente tiene todas las relaciones, se hace el save por el cliente, y
				// por cascada se hace todo lo demás.
				clientRepository.save(client);

				System.out.println(client);
			});
		});
	}

}
