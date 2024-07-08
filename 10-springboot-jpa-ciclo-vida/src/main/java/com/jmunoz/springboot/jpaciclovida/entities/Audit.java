package com.jmunoz.springboot.jpaciclovida.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

// Clase que se puede integrar en las clases Entity para pasar los campos que aquí aparezcan.
// Se usan estas clases para tener aquí campos, como los de auditoría, que usan muchas otras entity, con la idea de reutilizar.
//
// IMPORTANTE: Si todos los campos aquí incluidos son null en BD entonces este componente será Null 
// y arrojará un error NullPointerException cuando en Person.java haga un audit.getUpdateAt() por ejemplo.
@Embeddable
public class Audit {

  @Column(name = "create_at")
  private LocalDateTime createAt;
  
  @Column(name = "update_at")
  private LocalDateTime updateAt;

  // Esta es una opción para que no de el error NullPointerException
  // Consiste en que NO crea el campo en BD, no lo va a recuperar de BD y no es null, por lo que el componente no es null.
  // https://stackoverflow.com/questions/1324266/can-i-make-an-embedded-hibernate-entity-non-nullable/1324391#1324391
  @Formula("0")
  private int dummy;

  public LocalDateTime getCreateAt() {
    return createAt;
  }

  public void setCreateAt(LocalDateTime createAt) {
    this.createAt = createAt;
  }

  public LocalDateTime getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(LocalDateTime updateAt) {
    this.updateAt = updateAt;
  }

  // Anotación del ciclo de vida de JPA. Con el podemos realizar alguna función
  // antes de guardar en BD.
  // Es obligatorio devolver void.
  // En este ejemplo vamos a guardar automáticamente el campo createAt
  @PrePersist
  public void prePersist() {
    System.out.println("Evento del ciclo de vida del Entity Pre-Presist");
    this.createAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    System.out.println("Evento del ciclo de vida del Entity Pre-Update");
    this.updateAt = LocalDateTime.now();
  }
}
