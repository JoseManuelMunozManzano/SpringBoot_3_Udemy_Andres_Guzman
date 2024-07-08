package com.jmunoz.springboot.jpa.springbootjpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "persons")
public class Person {

  // Para MySql y SqlServer usamos estrategia IDENTITY, que significa autoincremental.
  // Para PostgreSql y Oracle, se utiliza generalmente la estrategia SEQUENCE. Para PostgreSql a veces también se usa IDENTITY.
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Mapeo automático a name y lastname
  private String name;
  private String lastname;

  // Para los campos con nombre compuesto, en BBDD la convención es todo en minúsculas y separado por guión bajo.
  @Column(name = "programming_language")
  private String programmingLanguage;

  // Si tenemos un constructor con parámetros, TENEMOS que crear el constructor vacío, 
  // que maneja JPA o Hibernate para crear la instancia.
  public Person() {
  }

  public Person(Long id, String name, String lastname, String programmingLanguage) {
    this.id = id;
    this.name = name;
    this.lastname = lastname;
    this.programmingLanguage = programmingLanguage;
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
  public String getProgrammingLanguage() {
    return programmingLanguage;
  }
  public void setProgrammingLanguage(String programmingLanguage) {
    this.programmingLanguage = programmingLanguage;
  }

  @Override
  public String toString() {
    return "[id=" + id + ", name=" + name + ", lastname=" + lastname + ", programmingLanguage="
        + programmingLanguage + "]";
  }
}
