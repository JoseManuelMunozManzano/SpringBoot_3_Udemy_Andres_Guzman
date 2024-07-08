package com.jmunoz.springboot.app.springbootcrud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Indicamos dónde se puede usar esta anotación
@Constraint(validatedBy = ExistsByUsernameValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByUsername {

	String message() default "ya existe en la base de datos. Escoja otro Username!";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
