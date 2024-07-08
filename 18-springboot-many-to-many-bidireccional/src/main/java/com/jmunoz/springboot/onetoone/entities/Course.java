package com.jmunoz.springboot.onetoone.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String instructor;

  // Establecemos la relación inversa (mappedBy) indicando el nombre del campo que indicamos
  // para el tipo List<Course> en la entity Student.
  @ManyToMany(mappedBy = "courses")
  private List<Student> students;
  
  public Course() {
    this.students = new ArrayList<>();
  }

  public Course(String name, String instructor) {
    this();
    this.name = name;
    this.instructor = instructor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInstructor() {
    return instructor;
  }

  public void setInstructor(String instructor) {
    this.instructor = instructor;
  }

  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }

  // Como el curso es dependiente del estudiante, para, dado el padre Student, poder eliminar
  // sus hijos, para poder comparar, necesitamos incluir hashCode y equals
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((instructor == null) ? 0 : instructor.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Course other = (Course) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (instructor == null) {
      if (other.instructor != null)
        return false;
    } else if (!instructor.equals(other.instructor))
      return false;
    return true;
  }

  // Recordar que para evitar dependencias circulares, solo una de las tablas puede tener
  // la relación. En este caso, en Student aparecen los Courses, por lo que aquí NO indicamos
  // los Students.
  @Override
  public String toString() {
    return "{id=" + id + ", name=" + name + ", instructor=" + instructor + "}";
  }


}
