package com.jmunoz.springboot.error.springbooterror.models;

import java.util.Date;

// Clase DTO con los atributos que mandaremos como JSON cuando ocurra una excepción
public class Error {
  
  private String message;
  private String error;
  private int status;
  private Date date;

  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getError() {
    return error;
  }
  public void setError(String error) {
    this.error = error;
  }
  public int getStatus() {
    return status;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
}
