package com.jmunoz.springboot.onetoone.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.springboot.onetoone.entities.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {
  
}
