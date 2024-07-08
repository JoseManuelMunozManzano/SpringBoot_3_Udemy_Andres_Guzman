package com.jmunoz.springboot.app.springbootcrud.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmunoz.springboot.app.springbootcrud.services.ProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class IsExistsBDValidation implements ConstraintValidator<IsExistsBD, String> {

  @Autowired
  private ProductService service;

  // Cuidado con este error al hacer el save porque hace una doble validación (javax y spring) y falla el save (POST)
  // https://stackoverflow.com/questions/37958145/autowired-gives-null-value-in-custom-constraint-validator
  //
  // Arreglo con properties:
  // https://stackoverflow.com/questions/56991490/how-to-save-object-having-field-with-custom-validator-using-spring-boots
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return !service.existsBySku(value);
  }
}
