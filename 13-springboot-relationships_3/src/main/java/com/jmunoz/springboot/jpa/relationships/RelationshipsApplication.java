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
			// oneToManyFindById(clientRepository);
			// removeAddress(clientRepository);
			// removeAddressFindById(clientRepository);
			removeAddressFindOne(clientRepository);
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

	// Eliminar objetos dependientes o hijos en la relación OneToMany.
	@Transactional
	private void removeAddress(ClientRepository clientRepository) {
		Client client = new Client("Frank", "Moras");

		Address address1 = new Address("El Verjel", 1234);
		Address address2 = new Address("Vasco de Gama", 9875);

		// Asignamos direcciones al cliente
		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		// De forma automática guarda las direcciones, gracias al cascade.
		clientRepository.save(client);

		System.out.println(client);

		// En esta búsqueda recuperamos el cliente, pero no sus direcciones.
		// Consulta lazy (obtener datos de forma tardía) Solo obtendrá la dirección cuando se le llame,
		// utilizando el método get.
		// Esto es para no tener que hacer más consultas de las necesarias.
		Optional<Client> optionalClient = clientRepository.findById(3L);
		optionalClient.ifPresent(c -> {
			// Consulta lazy.
			// Eliminando una dirección de la lista.
			//
			// NOTA: Esto falla en una app de consola o de escritorio, pero no web.
			//   El motivo es que tras el findById anterior se cierra la conexión.
			//   El error que indica es: could not initialize proxy - no Session
			// Para evitar este error, en application.properties añadimos la siguiente propiedad:
			//  spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
			// Otra forma de evitar este error, NO RECOMENDABLE, es, en Client.java, propiedad addresses, 
			//   cambiar el tipo de Fetch a EAGER.
			//
			// Y veremos que sigue sin eliminarse porque el address1 que se crea arriba, 
			// al persistirse, NO es la misma referencia, es decir, arriba al asignar direcciones, se generan
			// unas referencias. Al persistirse en el save() cambia la referencia, y el address de aquí abajo
			// es una referencia distinta.
			// Tenemos que cambiar el comportamiento por defecto a la hora de comparar dos objetos, para que
			// no lo haga por el valor de la referencia, sino por el id.
			// Para eso, se añade en la entity Address.java los override a los métodos hashCode() y equals()
			//
			// También es importante haber indicado la opción orphanRemoval en el entity Client.java
			c.getAddresses().remove(address1);
			// Haciendo efectivo el update para que borre esa dirección.
			clientRepository.save(c);
			System.out.println(c);
		});
	}

	// Eliminar objetos dependientes OneToMany con relación existente.
	@Transactional
	private void removeAddressFindById(ClientRepository clientRepository) {
		Optional<Client> optionalClient = clientRepository.findById(2L);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("El Verjel", 1234);
			Address address2 = new Address("Vasco de Gama", 9875);
	
			// Para que funcione, asignamos con el setter las direcciones.
			client.setAddresses(Arrays.asList(address1, address2));
	
			// De forma automática crea las direcciones y su foreign key, gracias al cascade.
			Client clientDB = clientRepository.save(client);
	
			System.out.println(clientDB);

			Optional<Client> optionalClient2 = clientRepository.findById(2L);
			optionalClient2.ifPresent(c -> {
				// Ahora mismo, ambos Address tienen dirección nula porque la consulta es lazy.
				// Para borrar, obtenemos el id del Address que queremos eliminar.
				Address address3 = c.getAddresses().get(1);

				// Consulta lazy.
				c.getAddresses().remove(address3);
				// Haciendo efectivo el update para que borre esa dirección.
				clientRepository.save(c);
				System.out.println(c);
			});
		});
	}

	// Eliminar objetos dependientes OneToMany con relación existente, pero usando consulta personalizada.
	// FORMA RECOMENDADA
	@Transactional
	private void removeAddressFindOne(ClientRepository clientRepository) {
		Optional<Client> optionalClient = clientRepository.findById(2L);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("El Verjel", 1234);
			Address address2 = new Address("Vasco de Gama", 9875);
	
			// Para que funcione, asignamos con el setter las direcciones.
			client.setAddresses(Arrays.asList(address1, address2));
	
			// De forma automática crea las direcciones y su foreign key, gracias al cascade.
			Client clientDB = clientRepository.save(client);
	
			System.out.println(clientDB);

			// Usando consulta personalizada con el join fetch
			// Solo es una consulta que trae todas sus direcciones. Está optimizado.
			// Ya no usa LAZY, pero tampoco es EAGER porque no son varias consultas.
			Optional<Client> optionalClient2 = clientRepository.findOne(2L);
			optionalClient2.ifPresent(c -> {
				// De los dos index que ha recuperado, el 0 y el 1, borro el 1.
				 c.getAddresses().remove(1);
				// Haciendo efectivo el update para que borre esa dirección.
				clientRepository.save(c);
				System.out.println(c);
			});
		});
	}
}
