package com.jmunoz.springboot.app.springbootcrud.validation;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Indicamos en ConstraintValidator el tipo de la anotación y el tipo del campo que se va a validar.
public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // Devolvemos si es o no válido.
    // return value != null && !value.isEmpty() && !value.isBlank();

    // Otra forma de validar lo mismo que hay arriba, usando StringUtils
    return StringUtils.hasText(value);
  }
  
}
