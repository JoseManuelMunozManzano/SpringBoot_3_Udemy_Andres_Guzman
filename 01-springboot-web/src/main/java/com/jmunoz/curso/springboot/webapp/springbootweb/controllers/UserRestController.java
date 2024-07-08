package com.jmunoz.curso.springboot.webapp.springbootweb.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jmunoz.curso.springboot.webapp.springbootweb.models.User;
import com.jmunoz.curso.springboot.webapp.springbootweb.models.dto.UserDto;

@RestController
@RequestMapping("/api")
public class UserRestController {

  @RequestMapping(path = "/details", method = RequestMethod.GET)
  public Map<String, Object> details() {

    Map<String, Object> body = new HashMap<>();
    body.put("title", "Hola Mundo Spring Boot");
    body.put("name", "José Manuel");
    body.put("lastname", "Muñoz Manzano");

    return body;
  }

  @GetMapping("/details2")
  public Map<String, Object> details2() {

    User user = new User("José Manuel", "Muñoz Manzano");
    Map<String, Object> body = new HashMap<>();

    body.put("title", "Hola Mundo Spring Boot");
    body.put("user", user);

    return body;
  }

  @GetMapping("/details3")
  public UserDto details3() {

    User user = new User("José Manuel", "Muñoz Manzano");
    
    UserDto userDto = new UserDto();
    userDto.setUser(user);
    userDto.setTitle("Hola Mundo Spring Boot");
    
    return userDto;
  }
  
  @GetMapping("/list")
  public List<User> list() {
    
    // Más adelante en el curso, esto se manejará en las capas Service y Repository
    User user = new User("José Manuel", "Muñoz Manzano");
    User user2 = new User("Pepe", "Doe");
    User user3 = new User("John", "Doe");

    List<User> users = Arrays.asList(user, user2, user3);

    return users;
  }
}
