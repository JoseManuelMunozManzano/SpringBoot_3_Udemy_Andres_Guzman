package com.jmunoz.springboot.onetoone.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.onetoone.entities.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {

  @Query("select s from Student s left join fetch s.courses where s.id = ?1")
  Optional<Student> findOneWithCourses(Long id);
}
