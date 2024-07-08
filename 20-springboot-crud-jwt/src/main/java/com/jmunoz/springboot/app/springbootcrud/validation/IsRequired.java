package com.jmunoz.springboot.app.springbootcrud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = RequiredValidation.class)
public @interface IsRequired {

	String message() default "es requerido usando anotación personalizada.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
