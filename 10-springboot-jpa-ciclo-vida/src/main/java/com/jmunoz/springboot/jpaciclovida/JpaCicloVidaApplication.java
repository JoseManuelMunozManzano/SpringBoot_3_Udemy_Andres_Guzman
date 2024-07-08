package com.jmunoz.springboot.jpaciclovida;

import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.jpaciclovida.entities.Person;
import com.jmunoz.springboot.jpaciclovida.repositories.PersonRepository;

@SpringBootApplication
public class JpaCicloVidaApplication implements CommandLineRunner {

	@Autowired
	private PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(JpaCicloVidaApplication.class, args);
	}

	public void run(String... args) throws Exception {
		// create();
		update();
	}

	// La fecha de alta se creará de manera automática gracias a los eventos del ciclo de vida
	// añadidos en el entity Person.
	@Transactional
	private void create() {

		Scanner scanner = new Scanner(System.in);

		System.out.println("Ingrese el nombre:");
		String name = scanner.next();
		System.out.println("Ingrese el apellido:");
		String lastname = scanner.next();
		System.out.println("Ingrese el lenguaje de programación:");
		String programmingLanguage = scanner.next();

		scanner.close();

		Person person = new Person(null, name, lastname, programmingLanguage);

		Person personNew = repository.save(person);
		System.out.println(personNew);

		repository.findById(personNew.getId()).ifPresent(System.out::println);
	}

	private void update() {

		Scanner scanner = new Scanner(System.in);

		System.out.println("Ingrese el id de la persona:");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		if (optionalPerson.isPresent()) {
			Person personDB = optionalPerson.orElseThrow();

			System.out.println(personDB);

			System.out.println("Ingrese el lenguaje de programación:");
			String programmingLanguage = scanner.next();
			personDB.setProgrammingLanguage(programmingLanguage);
			Person personUpdated = repository.save(personDB);
			System.out.println(personUpdated);
		} else {
			System.out.println("El usuario no existe!");
		}

		scanner.close();
	}
}
