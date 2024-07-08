package com.jmunoz.springboot.jpql;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.jpql.dto.PersonDto;
import com.jmunoz.springboot.jpql.entities.Person;
import com.jmunoz.springboot.jpql.repositories.PersonRepository;

@SpringBootApplication
public class JpqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpqlApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(PersonRepository repository) {
		return runner -> {
			// personalizedQueries(repository);
			// personalizedQueries2(repository);
			// personalizedQueriesDistinct(repository);
			// personalizedQueriesConcatUpperAndLowercase(repository);
			// personalizedQueriesBetween(repository);
			// queriesFunctionAggregation(repository);
			// subqueries(repository);
			whereIn(repository);
		};
	}

	@Transactional(readOnly = true)
	private void whereIn(PersonRepository repository) {

		System.out.println("=============== consulta where in ===============");
		List<Person> persons = repository.getPersonByIds(List.of(1L, 2L, 5L, 7L));
		persons.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	private void subqueries(PersonRepository repository) {

		System.out.println("=============== consulta por el nombre más corto y su largo ===============");
		List<Object[]> registers = repository.getShorterName();
		registers.forEach(reg -> {
			String name = (String) reg[0];
			Integer length = (Integer) reg[1];
			System.out.println("name=" + name + ", length=" + length);
		});

		System.out.println("=============== consulta para obtener el último registro de persona ===============");
		Optional<Person> optionalPerson = repository.getLastRegistration();
		optionalPerson.ifPresent(System.out::println);
	}

	@Transactional(readOnly = true)
	public void queriesFunctionAggregation(PersonRepository repository) {
		
		System.out.println("=============== consulta con el total de registros de la tabla persona ===============");
		Long count = repository.getTotalPerson();
		System.out.println(count);

		System.out.println("=============== consulta con el valor mínimo del id ===============");
		Long min = repository.getMinId();
		System.out.println(min);

		System.out.println("=============== consulta con el valor máximo del id ===============");
		Long max = repository.getMaxId();
		System.out.println(max);

		System.out.println("=============== consulta con el nombre y su largo ===============");
		List<Object[]> regs = repository.getPersonNameLength();
		regs.forEach(reg -> {
			String name = (String) reg[0];
			Integer length = (Integer) reg[1];
			System.out.println("name=" + name + ", length=" + length);
		});

		System.out.println("=============== consulta con el nombre más corto ===============");
		Integer minLengthName = repository.getMinLengthName();
		System.out.println(minLengthName);

		System.out.println("=============== consulta con el nombre más largo ===============");
		Integer maxLengthName = repository.getMaxLengthName();
		System.out.println(maxLengthName);

		System.out.println("=============== consultas resumen de funciones de agregación: min, max, sum, avg, count ===============");
		Object[] resumeReg = (Object[]) repository.getResumeAggregationFunction();
		System.out.println("min=" + resumeReg[0] + 
		                  ", max=" + resumeReg[1] + 
											", sum=" + resumeReg[2] + 
											", avg=" + resumeReg[3] + 
											", count=" + resumeReg[4]
										);
	}

	@Transactional(readOnly = true)
	public void personalizedQueriesBetween(PersonRepository repository) {

		System.out.println("=============== consulta por rangos y order by ===============");
		List<Person> persons = repository.findAllBetweenId(2L, 5L);
		persons.forEach(System.out::println);
		
		persons = repository.findAllBetweenName("J", "P");
		persons.forEach(System.out::println);
		
		System.out.println("=============== consulta por rangos usando Query Method ===============");
		persons = repository.findByIdBetween(2L, 5L);
		persons.forEach(System.out::println);
		
		persons = repository.findByNameBetween("J", "P");
		persons.forEach(System.out::println);
		
		System.out.println("=============== consulta por rangos y order by usando Query Method ===============");
		persons = repository.findByIdBetweenOrderByIdDesc(2L, 5L);
		persons.forEach(System.out::println);
		
		persons = repository.findByNameBetweenOrderByNameAscLastnameDesc("J", "P");
		persons.forEach(System.out::println);
		
		System.out.println("=============== consulta con order by ===============");
		persons = repository.getAllOrdered();
		persons.forEach(System.out::println);
		
		System.out.println("=============== consulta con order by usando Query Method ===============");
		persons = repository.findAllByOrderByNameAscLastnameDesc();
		persons.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	public void personalizedQueriesConcatUpperAndLowercase(PersonRepository repository) {

		System.out.println("=============== consulta nombres y apellidos de personas ===============");
		List<String> names = repository.findAllFullNameConcat();
		names.forEach(System.out::println);

		System.out.println("=============== consulta nombres y apellidos mayúsculas ===============");
		names = repository.findAllFullNameConcatUpper();
		names.forEach(System.out::println);
		
		System.out.println("=============== consulta nombres y apellidos minúsculas ===============");
		names = repository.findAllFullNameConcatLower();
		names.forEach(System.out::println);
		
		System.out.println("=============== consulta personalizada persona upper y lower case ===============");
		List<Object[]> personsDtos = repository.findAllPersonDataListCase();
		personsDtos.forEach(reg -> {
			System.out.println("id = " + reg[0] + 
			", nombre = " + reg[1] + 
			", apellido = " + reg[2] + 
			", lenguaje = " + reg[3]);
		});
	}

	@Transactional(readOnly = true)
	public void personalizedQueriesDistinct(PersonRepository repository) {

		System.out.println("=============== consulta con nombres únicos de personas ===============");
		List<String> names = repository.findAllNamesDistinct();
		names.forEach(System.out::println);

		System.out.println("=============== consulta con lenguajes de programación únicos ===============");
		List<String> programmingLanguages = repository.findAllProgrammingLanguageDistinct();
		programmingLanguages.forEach(System.out::println);

		System.out.println("=============== consulta con total de lenguajes de programación únicos ===============");
		Long totalLanguages = repository.findAllProgrammingLanguageDistinctCount();
		System.out.println("total de lenguajes de programación: " + totalLanguages);
	}

	@Transactional(readOnly = true)
	public void personalizedQueries2(PersonRepository repository) {

		System.out.println("=============== consulta por objeto persona y lenguaje de programación ===============");
		List<Object[]> personsRegs = repository.findAllMixPerson();
		
		// Es importante saber en qué posición del array está cada cosa.
		// programmingLanguage está en la posición 1 y el objeto person en la posición 0
		personsRegs.forEach(reg -> {
			System.out.println("programmingLanguage=" + reg[1] + ", person=" + reg[0]);
		});
		
		System.out.println("=============== consulta que puebla y devuelve objeto entity de una instacia personalizada ===============");
		List<Person> persons = repository.findAllObjectPersonPersonalized();
		persons.forEach(System.out::println);

		System.out.println("=============== consulta que puebla y devuelve objeto dto de una instacia dto personalizada ===============");
		List<PersonDto> personsDto = repository.findAllPersonDto();
		personsDto.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	public void personalizedQueries(PersonRepository repository) {

		Scanner scanner = new Scanner(System.in);

		System.out.println("=============== consulta solo el nombre por el id ===============");
		System.out.println("Ingrese el id:");
		Long id = scanner.nextLong();
		scanner.close();
		
		System.out.println("=============== mostrando solo el nombre ===============");
		String name = repository.getNameById(id);
		System.out.println(name);
		
		System.out.println("=============== mostrando solo el id ===============");
		Long idDb = repository.getIdById(id);
		System.out.println(idDb);
		
		System.out.println("=============== mostrando nombre completo ===============");
		String fullName = repository.getFullNameById(id);
		System.out.println(fullName);

		System.out.println("=============== consulta por campos personalizados por el id ===============");
		// Aquí convertimos a array de Objeto
		Object[] personReg = (Object[]) repository.obtenerPersonDataById(id);
		System.out.println("id = " + personReg[0] + 
		", nombre = " + personReg[1] + 
		", apellido = " + personReg[2] + 
		", lenguaje = " + personReg[3]);
		
		System.out.println("=============== consulta por campos personalizados por el id con Optional ===============");
		Optional<Object> personRegOptional = repository.obtenerPersonDataByIdOptional(id);
		if (personRegOptional.isPresent()) {
			Object[] personReg2 = (Object[]) personRegOptional.orElseThrow();
			System.out.println("id = " + personReg2[0] + 
			", nombre = " + personReg2[1] + 
			", apellido = " + personReg2[2] + 
			", lenguaje = " + personReg2[3]);		
		}
  
		System.out.println("=============== consulta por campos personalizados - lista ===============");
		List<Object[]> regs = repository.obtenerPersonDataList();
		regs.forEach(reg -> {
			System.out.println("id = " + reg[0] + 
			", nombre = " + reg[1] + 
			", apellido = " + reg[2] + 
			", lenguaje = " + reg[3]);
		});
	}
}
