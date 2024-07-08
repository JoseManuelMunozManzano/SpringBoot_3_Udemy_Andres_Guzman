package com.jmunoz.curso.springboot.webapp.springbootweb.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api2")
public class UserRestController2 {

  @GetMapping("/details")
  @ResponseBody
  public Map<String, Object> details() {

    Map<String, Object> body = new HashMap<>();
    body.put("title", "Hola Mundo Spring Boot");
    body.put("name", "José Manuel");
    body.put("lastname", "Muñoz Manzano");

    return body;
  }
}
