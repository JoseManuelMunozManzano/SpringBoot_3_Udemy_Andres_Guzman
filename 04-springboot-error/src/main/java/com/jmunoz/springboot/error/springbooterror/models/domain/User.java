package com.jmunoz.springboot.error.springbooterror.models.domain;

public class User {

  private Long id;
  private String name;
  private String lastname;
  private Role role;

  public User() {
  }

  public User(Long id, String name, String lastname) {
    this.id = id;
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
  public Role getRole() {
    return role;
  }
  public void setRole(Role role) {
    this.role = role;
  }

  // Esto lo hacemos para que role, que será null, genere una Excepción Null Pointer Exception.
  // Tendremos excepción en un id existente
  // Si queremos que genere excepción, simplemente se comenta este método
  public String getRoleName() {
    return role.getName();
  }
}
