package com.jmunoz.springboot.error.springbooterror.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.jmunoz.springboot.error.springbooterror.exceptions.UserNotFoundException;
import com.jmunoz.springboot.error.springbooterror.models.Error;

@RestControllerAdvice
public class HandlerExceptionController {

  // Para indicar cualquier tipo de error, sería ResponseEntity<?>
  @ExceptionHandler({ArithmeticException.class})
  public ResponseEntity<Error> divisionByZero(Exception ex) {

    Error error = new Error();
    error.setDate(new Date());
    error.setError("Error división por cero");
    error.setMessage(ex.getMessage());
    error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

    // Hay distintos métodos que podemos usar.
    //
    // Podemos usar internalServerError() porque es Postman hemos visto que ese es el tipo de error
    // de la división por cero. Corresponde al error 500
    //
    // return ResponseEntity.internalServerError().body(error);
    
    // Otra forma es usar el método status(), de nuevo pasando el error 500.
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
  }

  // Para error 404 no olvidarse de indicar en application.properties la propiedad spring.web.resources.add-mappings=false
  // En este caso, el argumento del método, en vez de ser un genérico Exception, es el que estamos manejando en concreto.
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Error> notFoundEx(NoHandlerFoundException ex) {

    Error error = new Error();
    error.setDate(new Date());
    error.setError("Api REST no encontrada");
    error.setMessage(ex.getMessage());
    error.setStatus(HttpStatus.NOT_FOUND.value());

    // Si no queremos personalizar el mensaje de error puedo indicar:
    // return ResponseEntity.notFound().build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
  }

  @ExceptionHandler(NumberFormatException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> numberFormatException(Exception ex) {

    Map<String, String> error = new HashMap<>();
    error.put("date", new Date().toString());
    error.put("error", "Número inválido o incorrecto, no tiene formato de dígito!");
    error.put("message", ex.getMessage());
    error.put("status", Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    // El status se indica en la anotación @ResponseStatus
    return error;
  }

  @ExceptionHandler({NullPointerException.class, HttpMessageNotWritableException.class, UserNotFoundException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> userNotFoundException(Exception ex) {

    Map<String, String> error = new HashMap<>();
    error.put("date", new Date().toString());
    error.put("error", "El usuario o role no existe!");
    error.put("message", ex.getMessage());
    error.put("status", Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    // El status se indica en la anotación @ResponseStatus
    return error;
  }
}
