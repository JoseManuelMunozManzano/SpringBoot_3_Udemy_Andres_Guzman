package com.jmunoz.springboot.app.springbootcrud;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jmunoz.springboot.app.springbootcrud.entities.Product;

@Component
public class ProductValidation implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Product.class.isAssignableFrom(clazz);
  }

  // El target es el objeto Product.
  // errors corresponde en el controlador al BindingResult (BindingResult extends Errors)
  @Override
  public void validate(Object target, Errors errors) {
    // Hacemos el cast
    Product product = (Product) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null, "es obligatorio.");

    // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", null, "es obligatorio.");
    //
    // Forma más programática de poner lo de arriba:
    if (product.getDescription() == null || product.getDescription().isBlank()) {
      errors.rejectValue("description", null, "es obligatorio.");
    }

    if (product.getPrice() == null) {
      errors.rejectValue("price", null, "es obligatorio.");
    } else if(product.getPrice() < 500) {
      errors.rejectValue("price", null, "tiene que ser mayor o igual a 500.");
    }
  }
}
