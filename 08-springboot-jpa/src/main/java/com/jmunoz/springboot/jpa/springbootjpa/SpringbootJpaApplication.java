package com.jmunoz.springboot.jpa.springbootjpa;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.jpa.springbootjpa.entities.Person;
import com.jmunoz.springboot.jpa.springbootjpa.repositories.PersonRepository;

@SpringBootApplication
public class SpringbootJpaApplication implements CommandLineRunner {

	@Autowired
	private PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Descomentar si queremos buscar
		// list();
		// findOne();

		// Descomentar si queremos insertar
		// create();

		// Descomentar si queremos actualizar
		update();

		// Descomentar si queremos eliminar por id
		// deleteById();

		// Descomentar si queremos eliminar usando entity
		// delete();
	}

	@Transactional
	public void delete() {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona a eliminar:");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		optionalPerson.ifPresentOrElse(repository::delete, 
			() -> {
				System.out.println("Persona no encontrada!");
			}
		);

		repository.findAll().forEach(System.out::println);

		scanner.close();
	}

	@Transactional
	public void deleteById() {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona a eliminar:");
		Long id = scanner.nextLong();

		repository.deleteById(id);

		repository.findAll().forEach(System.out::println);

		scanner.close();
	}

	@Transactional
	public void update() {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona:");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		optionalPerson.ifPresentOrElse(p -> {
			System.out.println(p);
			System.out.println("Ingrese el lenguaje de programación:");
			String programmingLanguage = scanner.next();
			p.setProgrammingLanguage(programmingLanguage);
			Person personUpdate = repository.save(p);
			System.out.println(personUpdate);
		},
		() -> {
			System.out.println("Persona no encontrada!");
		});

		scanner.close();
	}

	// Esto es un proyecto de consola, pero si fuera web, con un controller y un service, esta anotación @Transactional
	// iría en la clase service (que sería un componente Spring)
	// @Transactional no deja de ser un aspecto que envuelve el método. Antes de la ejecución hace un Begin Transaction
	// y al final hace un commit o rollback si hay algún error.
	@Transactional
	public void create() {

		Scanner scanner = new Scanner(System.in);

		System.out.println("Ingrese el nombre: ");
		String name = scanner.nextLine();
		System.out.println("Ingrese el apellido: ");
		String lastname = scanner.nextLine();
		System.out.println("Ingrese el lenguaje de programación: ");
		String programmingLanguage = scanner.nextLine();

		scanner.close();

		// En un alta, el id lo maneja la BBDD, lo va a crear. Por eso se manda a null
		Person person = new Person(null, name, lastname, programmingLanguage);

		// Si el id es nulo lo inserta. Si viene un valor lo actualiza.
		// save() devuelve un entity con el id.
		Person personNew = repository.save(person);
		System.out.println(personNew);

		repository.findById(personNew.getId()).ifPresent(System.out::println);
	}

	// Lecturas que no modifican la BBDD se anotan de esta manera.
	@Transactional(readOnly = true)
	public void findOne() {

		// Person person = null;
		// Optional<Person> optionalPerson = repository.findById(1L);
		// if (optionalPerson.isPresent()) {
		// 	person = optionalPerson.get();
		// }
		// System.out.println(person);

		repository.findById(1L).ifPresent(System.out::println);
		repository.findOne(1L).ifPresent(System.out::println);
		repository.findOneName("Pepe").ifPresent(System.out::println);
		repository.findOneLikeName("Pe").ifPresent(System.out::println);
		repository.findByName("John").ifPresent(System.out::println);
		repository.findByNameContaining("ria").ifPresent(System.out::println);
	}

	@Transactional(readOnly = true)
	public void list() {
		List<Person> persons;
		List<Object[]> personsValues;

		// Comentar/descomentar para probar

		// findAll() devuelve un iterable que debemos convertir a un tipo List.
		// persons = (List<Person>) repository.findAll();
		
		// Usando un Query Method (ver PersonRepository.java)
		// persons = (List<Person>) repository.findByProgrammingLanguage("Java");

		persons = (List<Person>) repository.findByProgrammingLanguageAndName("Python", "Pepe");

		// Usando anotación @Query
		// persons = (List<Person>) repository.buscarByProgrammingLanguageAndName("JavaScript", "John");

		persons.stream().forEach(System.out::println);

		// Recuperando campos concretos del objeto entity
		personsValues = repository.obtenerPersonData();
		personsValues.stream().forEach(p -> {
			System.out.println(p[0] + " es experto en " + p[1]);
		});
	}
}
