package com.jmunoz.springboot.app.springbootcrud.validation;

import org.springframework.stereotype.Component;

import com.jmunoz.springboot.app.springbootcrud.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Tenemos que indicar que es un componente, para poder inyectar el repositorio y poder validar.
// En ConstraintValidator tenemos que indicar la anotación, y el tipo de dato por el que estamos validando.
@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

  private UserService service;

  // @Autowired usando el constructor
  public ExistsByUsernameValidation(UserService service) {
    this.service = service;
  }

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {

    // Si da error descomentar la línea de abajo.
    // Se añade esta validación porque parece que da error por una doble validación.
    // La primera vez valida pero en una segunda validación service vale null
    // if (service == null) return true;

    return !service.existsByUsername(username);
  }
  
}
