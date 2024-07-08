package com.jmunoz.springboot.app.springbootcrud.validation;

import org.springframework.stereotype.Component;

import com.jmunoz.springboot.app.springbootcrud.services.ProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class IsExistsBDValidation implements ConstraintValidator<IsExistsBD, String> {

  private ProductService service;
  private boolean isUpdate;

  // Uso de @Autowired implícito en constructor
  public IsExistsBDValidation(ProductService service) {
    this.service = service;
  }

  @Override
  public void initialize(IsExistsBD constraintAnnotation) {
    isUpdate = constraintAnnotation.isUpdate();
  }

  // Cuidado con este error al hacer el save porque hace una doble validación (javax y spring) y falla el save (POST)
  // https://stackoverflow.com/questions/37958145/autowired-gives-null-value-in-custom-constraint-validator
  //
  // Arreglo con properties:
  // https://stackoverflow.com/questions/56991490/how-to-save-object-having-field-with-custom-validator-using-spring-boots
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // Si da error descomentar la línea de abajo.
    // Se añade esta validación porque parece que da error por una doble validación.
    // La primera vez valida pero en una segunda validación service vale null
    if (service == null || isUpdate) return true;
  
    return !service.existsBySku(value);
  }
}
