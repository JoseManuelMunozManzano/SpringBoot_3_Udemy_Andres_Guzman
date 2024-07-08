package com.jmunoz.springboot.onetoone;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.onetoone.entities.Client;
import com.jmunoz.springboot.onetoone.entities.ClientDetails;
import com.jmunoz.springboot.onetoone.repositories.ClientDetailsRepository;
import com.jmunoz.springboot.onetoone.repositories.ClientRepository;

@SpringBootApplication
public class OneToOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneToOneApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ClientRepository clientRepository, ClientDetailsRepository clientDetailsRepository) {

		return runner -> {
			// oneToOne(clientRepository, clientDetailsRepository);
			oneToOneFindById(clientRepository, clientDetailsRepository);
		};
	}

	@Transactional
	public void oneToOne(ClientRepository clientRepository, ClientDetailsRepository clientDetailsRepository) {

		// Creamos detalle del cliente y lo guardamos.
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);

		// Creamos el cliente, asignando su detalle, y lo guardamos.
		// Client es el dueño de la relación, es donde va la llave foranea.
		Client client = new Client("Erba", "Pura");
		client.setClientDetails(clientDetails);
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void oneToOneFindById(ClientRepository clientRepository, ClientDetailsRepository clientDetailsRepository) {

		// Creamos detalle del cliente y lo guardamos.
		// Si fuera LAZY (es EAGER), más abajo, al hacer el toString NO da error porque aquí se persiste y lo tenemos en el
		// contexto de persistencia y no lo necesita ir a buscar.
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);

		// Buscamos el cliente, asignando un detalle, y lo guardamos.
		// Client es el dueño de la relación, es donde va la llave foranea.
		Optional<Client> clientOptional = clientRepository.findById(2L);
		clientOptional.ifPresent(client -> {
			client.setClientDetails(clientDetails);
			clientRepository.save(client);
	
			System.out.println(client);
		});
	}
}
