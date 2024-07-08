package com.jmunoz.springboot.onetoone.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// Student es la clase padre o principal que contiene una lista de cursos como sus hijos.
// Muchos estudiantes pueden tener muchos cursos y, a su vez, un curso puede estar asignado a muchos estudiantes.
@Entity
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String lastname;

  // Vamos a empezar a hacer un @ManyToMany unidireccional.
  //
  // Solamente el lado del estudiante tendrá la relación.
  // El cascade puede ser PERSIST y MERGE, pero NO ALL.
  // Esto es porque, si eliminamos un estudiante, no podemos eliminar los cursos, ya que
  // dichos cursos pueden estar asignados a otros estudiantes.
  //
  // Si no se indica @JoinTable, se genera una tabla intermedia con nombre por defecto 
  // students_courses y campos por defecto courses_id y student_id
  //
  // Para cambiar los nombres por defecto usamos @JoinTable.
  // En este ejemplo, el nombre de la tabla será tbl_alumnos_cursos.
  // Con joinColumns indicamos la foreign key de la clase principal (Student), en este caso alumno_id,
  // y la foreign key de la relación inversa (Course), en este caso curso_id.
  // Por último colocamos el uniqueConstraints, con un arreglo que contiene los campos que 
  // no se pueden repetir (clave primaria)
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "tbl_alumnos_cursos", 
             joinColumns = @JoinColumn(name = "alumno_id"), 
             inverseJoinColumns = @JoinColumn(name = "curso_id"),
             uniqueConstraints = @UniqueConstraint(columnNames = {"alumno_id", "curso_id"})
            )
  private List<Course> courses;

  public Student() {
    this.courses = new ArrayList<>();
  }

  public Student(String name, String lastname) {
    this();
    this.name = name;
    this.lastname = lastname;
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

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }

  @Override
  public String toString() {
    return "{id=" + id + ", name=" + name + ", lastname=" + lastname + ", courses=" + courses + "}";
  }
}
