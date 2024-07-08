package com.jmunoz.springboot.error.springbooterror.exceptions;

// Si heredamos de Exception nos obligará a manejar la excepción con try ... catch
// Si heredamos de RuntimeException no hará falta el try ... catch, pudiendo manejar 
// la excepción en el controller HandlerExceptionController y la anotación @RestControllerAdvice
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
