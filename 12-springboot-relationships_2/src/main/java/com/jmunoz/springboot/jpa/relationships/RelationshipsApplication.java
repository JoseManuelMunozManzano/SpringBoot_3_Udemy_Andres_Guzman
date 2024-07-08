package com.jmunoz.springboot.jpa.relationships;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.jpa.relationships.entities.Address;
import com.jmunoz.springboot.jpa.relationships.entities.Client;
import com.jmunoz.springboot.jpa.relationships.repositories.ClientRepository;

@SpringBootApplication
public class RelationshipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelationshipsApplication.class, args);
	}

  @Bean
  CommandLineRunner commandLineRunner(ClientRepository clientRepository) {

		return runner -> {
			// oneToMany(clientRepository);
			oneToManyFindById(clientRepository);
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
	private void oneToManyFindById(ClientRepository clientRepository) {
		Optional<Client> optionalClient = clientRepository.findById(2L);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("El Verjel", 1234);
			Address address2 = new Address("Vasco de Gama", 9875);
	
			// No tenemos dirección, solo cliente, ya que la dirección se busca de forma
			// perezosa, es decir, solo bajo petición. Con el getAddresses nos dará un error
			// diciendo que JPA ha cerrado la conexión.
			// Es decir, esto fallaría:
			// client.getAddresses().add(address1);
			//
			// Y para que funcione, asignamos con el setter las direcciones.
			client.setAddresses(Arrays.asList(address1, address2));
	
			// De forma automática crea las direcciones y su foreign key, gracias al cascade. 
			Client clientDB = clientRepository.save(client);
	
			System.out.println(clientDB);
		});
	}
}
