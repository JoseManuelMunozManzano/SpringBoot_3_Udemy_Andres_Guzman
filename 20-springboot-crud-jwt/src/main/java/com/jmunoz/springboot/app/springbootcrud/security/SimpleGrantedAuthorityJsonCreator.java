package com.jmunoz.springboot.app.springbootcrud.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator {

  // El constructor recibe el tipo String role porque es el constructor original SimpleGrantedAuthority.
  // Tenemos que indicarle que pueble del JSON el atributo "authority" en el parámetro "role"
  @JsonCreator
  public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {

  }
}
