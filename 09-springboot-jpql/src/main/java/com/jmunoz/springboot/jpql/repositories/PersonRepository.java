package com.jmunoz.springboot.jpql.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.jpql.dto.PersonDto;
import com.jmunoz.springboot.jpql.entities.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

  // Consultas personalizadas que no están basadas en el nombre del método
  @Query("select p.name from Person p where p.id=?1")
  String getNameById(Long id);

  @Query("select p.id from Person p where p.id=?1")
  Long getIdById(Long id);

  @Query("select p.name || ' ' || p.lastname from Person p where p.id=?1")
  String getFullNameById(Long id);

  // Obtener todo el objeto desarmado. Una lista
  @Query("select p.id, p.name, p.lastname, p.programmingLanguage from Person p")
  List<Object[]> obtenerPersonDataList();
  
  // Obtener todo el objeto desarmado. Un solo registro.
  // Con Spring JPA, no hace falta indicar los corchetes cuando se recupera un solo objeto.
  // Igualmente, al recuperarlo luego en la clase JpqlApplication, hay que convertirlo a array.
  @Query("select p.id, p.name, p.lastname, p.programmingLanguage from Person p where p.id = :id")
  Object obtenerPersonDataById(Long id);
  
  // Se puede indicar que es un Optional, pero sigue sin poder indicarse los corchetes
  // cuando solo se recupera un registro
  @Query("select p.id, p.name, p.lastname, p.programmingLanguage from Person p where p.id = :id")
  Optional<Object> obtenerPersonDataByIdOptional(Long id);

  // Combinación de objeto y campos separados
  @Query("select p, p.programmingLanguage from Person p")
  List<Object[]> findAllMixPerson();

  // Devolver una lista de Person, pero que solo contenga el nombre y el apellido.
  // Para poder hacer esto necesitamos un constructor en la Entity que tenga solo name y lastname.
  // Es importante ver que se selecciona una instanciación de Person hecha por nosotros mismos y no de
  // forma interna por JPA  (como ocurre en todos los otros ejemplos que hemos visto)
  // Por eso tiene que existir ese constructor.
  @Query("select new Person(p.name, p.lastname) from Person p")
  List<Person> findAllObjectPersonPersonalized();

  // Devolver una lista de una clase DTO, un objeto que sería como un Entity, pero con distintas transformaciones
  // o formateos necesarias para que las consuma el cliente.
  // Hay que indicar el package completo en la query porque no se inyecta, ya que un DTO no es un componente de Spring.
  @Query("select new com.jmunoz.springboot.jpql.dto.PersonDto(p.name, p.lastname) from Person p")
  List<PersonDto> findAllPersonDto();

  // Uso de DISTINCT
  @Query("select distinct(p.name) from Person p")
  List<String> findAllNamesDistinct();

  @Query("select distinct(p.programmingLanguage) from Person p")
  List<String> findAllProgrammingLanguageDistinct();

  // Uso de count y distinct
  @Query("select count(distinct(p.programmingLanguage)) from Person p")
  Long findAllProgrammingLanguageDistinctCount();

  // Uso de concat (también vale ||)
  // @Query("select p.name || ' ' || p.lastname from Person p")
  @Query("select concat(p.name, ' ', p.lastname) from Person p")
  List<String> findAllFullNameConcat();

  // Uso de upper
  @Query("select upper(p.name || ' ' || p.lastname) from Person p")
  List<String> findAllFullNameConcatUpper();

  // Uso de lower
  @Query("select lower(concat(p.name, ' ', p.lastname)) from Person p")
  List<String> findAllFullNameConcatLower();

  // Mezcla de lower y upper
  @Query("select p.id, upper(p.name), lower(p.lastname), upper(p.programmingLanguage) from Person p")
  List<Object[]> findAllPersonDataListCase();

  // between y order by
  @Query("select p from Person p where p.id between :id1 and :id2 order by p.name asc")
  List<Person> findAllBetweenId(Long id1, Long id2);
  
  // La P no se incluye
  // @Query("select p from Person p where p.name between 'J' and 'P'")
  @Query("select p from Person p where p.name between ?1 and ?2 order by p.name desc, p.lastname asc")
  List<Person> findAllBetweenName(String c1, String c2);

  // between usando Query Method
  List<Person> findByIdBetween(Long id1, Long id2);

  List<Person> findByNameBetween(String name1, String name2);
  
  // between y order by usando Query Method
  List<Person> findByIdBetweenOrderByIdDesc(Long id1, Long id2);
  
  List<Person> findByNameBetweenOrderByNameAscLastnameDesc(String name1, String name2);

  // order by
  @Query("select p from Person p order by p.name desc, p.lastname")
  List<Person> getAllOrdered();

  // order by usando Query Method
  // Muy importante colocar el By tras All
  List<Person> findAllByOrderByNameAscLastnameDesc();

  // agregación: count, min, max
  // Indicar que en CrudRepository ya tenemos una función long count() que devuelve el total de los registros de la tabla.
  @Query("select count(p) from Person p")
  Long getTotalPerson();

  @Query("select min(p.id) from Person p")
  Long getMinId();

  @Query("select max(p.id) from Person p")
  Long getMaxId();

  // length

  // En el índice cero del array tendremos el nombre y en el índice 1 la longitud
  @Query("select p.name, length(p.name) from Person p")
  List<Object[]> getPersonNameLength();

  @Query("select min(length(p.name)) from Person p")
  Integer getMinLengthName();

  @Query("select max(length(p.name)) from Person p")
  Integer getMaxLengthName();

  // sum y avg
  @Query("select min(p.id), max(p.id), sum(p.id), avg(length(p.name)), count(p.id) from Person p")
  Object getResumeAggregationFunction();

  // Subconsultas
  @Query("select p.name, length(p.name) from Person p where length(p.name) = (select min(length(p.name)) from Person p)")
  List<Object[]> getShorterName();

  @Query("select p from Person p where p.id = (select max(p.id) from Person p)")
  Optional<Person> getLastRegistration();

  // where in
  @Query("select p from Person p where p.id in ?1")
  List<Person> getPersonByIds(List<Long> ids);
}
