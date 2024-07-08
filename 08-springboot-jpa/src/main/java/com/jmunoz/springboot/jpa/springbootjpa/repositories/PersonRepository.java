package com.jmunoz.springboot.jpa.springbootjpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.jpa.springbootjpa.entities.Person;
import java.util.List;
import java.util.Optional;


// Se indica el tipo de la clase Entity y el tipo de dato de la llave primaria, campo id en este ejemplo.
// Al extender de CrudRepository ya es un componente de Spring y se puede inyectar.
public interface PersonRepository extends CrudRepository<Person, Long> {

  // Consultas personalizadas con anotación @Query y nomenclatura Query Methods
  // Ejemplo de búsqueda por atributo
  List<Person> findByProgrammingLanguage(String programmingLanguage);

  List<Person> findByProgrammingLanguageAndName(String programmingLanguage, String name);
  
  // buscar, en español, no cumple con la nomenclatura de Query Method, por lo que no va a funcionar.
  // Usamos la anotación @Query con una consulta JPA, usando la clase entity, no la tabla.
  //
  // Usando el orden de los parámetros
  //
  //@Query("select p from Person p where p.programmingLanguage = ?1 and p.name = ?2")
  //
  // Usando el nombre del parámetro
  @Query("select p from Person p where p.programmingLanguage = :programmingLanguage and p.name = :name")
  List<Person> buscarByProgrammingLanguageAndName(String programmingLanguage, String name);

  // Devolviendo campos concretos y valores del objeto entity.
  // Lo normal es recuperar todo el objeto y luego, usando los get del objeto, obtener el valor de lo que necesitamos.
  // Pero se puede devolver valores de un objeto en vez del objeto.
  // Devuelve un array de objetos porque en el índice 0 estará name y en el índice 1 estará programmingLanguage.
  @Query("select p.name, p.programmingLanguage from Person p")
  List<Object[]> obtenerPersonData();

  // Devolver un solo objeto usando @Query, cuando no cumple con la nomenclatura de Query Method
  @Query("select p from Person p where p.id = :id")
  Optional<Person> findOne(Long id);

  @Query("select p from Person p where p.name = :name")
  Optional<Person> findOneName(String name);

  // Uso de like
  @Query("select p from Person p where p.name like %:name%")
  Optional<Person> findOneLikeName(String name);
  
  // Devolver un solo objeto, pero usando Query Methods
  Optional<Person> findByName(String name);

  Optional<Person> findByNameContaining(String name);
}
