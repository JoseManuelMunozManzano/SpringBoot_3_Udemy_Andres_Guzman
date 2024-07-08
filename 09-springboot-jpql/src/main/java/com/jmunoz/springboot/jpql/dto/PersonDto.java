package com.jmunoz.springboot.jpql.dto;

// clase DTO
// Sería como una clase personalizada, formateada y simplificada de una clase Entity, 
// y que vamos a devolver al cliente final.
// No se anota con nada. No deja de ser un POJO.
// Como es a nuestra conveniencia, no nos hace falta ni un constructor vacío.
public class PersonDto {

  private String name;
  private String lastname;

  public PersonDto(String name, String lastname) {
    this.name = name;
    this.lastname = lastname;
  }

  public String getName() {
    return name;
  }

  public String getLastname() {
    return lastname;
  }

  @Override
  public String toString() {
    return "[name=" + name + ", lastname=" + lastname + "]";
  }
}
