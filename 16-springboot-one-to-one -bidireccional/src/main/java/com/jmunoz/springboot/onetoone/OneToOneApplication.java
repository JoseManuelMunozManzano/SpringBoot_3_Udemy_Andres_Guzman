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
			// oneToOneBidireccional(clientRepository, clientDetailsRepository);
			oneToOneBidireccionalFindById(clientRepository, clientDetailsRepository);
		};
	}

	@Transactional
	public void oneToOneBidireccional(ClientRepository clientRepository, ClientDetailsRepository clientDetailsRepository) {

		// Creamos la clase principal o padre (el cliente) primero.
		Client client = new Client("Erba", "Pura");
		
		// Creamos detalle del cliente (el dueño de la relación)
		ClientDetails clientDetails = new ClientDetails(true, 5000);		
		
		// Asignamos al cliente su detalle y a su detalle el cliente (ver método setClientDetails)
		client.setClientDetails(clientDetails);

		// Se guarda el cliente y, en cascada, su detalle.
		// Client es el que maneja el cascade
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void oneToOneBidireccionalFindById(ClientRepository clientRepository, ClientDetailsRepository clientDetailsRepository) {

		// Buscamos el cliente. Con findById no traemos todas las relaciones, solo el cliente.
		Optional<Client> clientOptional = clientRepository.findById(2L);

		clientOptional.ifPresent(client -> {
			// Creamos detalle del cliente.
			ClientDetails clientDetails = new ClientDetails(true, 5000);
			
			client.setClientDetails(clientDetails);

			Client clientDB = clientRepository.save(client);
	
			// No falla aunque sea LAZY porque arriba se persiste y lo tenemos en el 
			// contexto de persistencia y no lo necesita ir a buscar.
			System.out.println(clientDB);
		});
	}
}
