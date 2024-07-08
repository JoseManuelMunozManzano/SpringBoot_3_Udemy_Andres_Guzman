package com.jmunoz.curso.springboot.webapp.springbootweb.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.jmunoz.curso.springboot.webapp.springbootweb.models.User;

@Controller
public class UserController {

  @GetMapping("/det")
  public String details() {

    return "details";
  }

  @GetMapping("/details")
  public String details2(Model model) {

    model.addAttribute("title", "Hola Mundo Spring Boot");
    model.addAttribute("name", "José Manuel");
    model.addAttribute("lastname", "Muñoz Manzano");

    return "details2";
  }

  @GetMapping("/details2")
  public String details3(Map<String, Object> model) {

    model.put("title", "Hola Mundo Spring Boot");
    model.put("name", "José Manuel");
    model.put("lastname", "Muñoz Manzano");

    return "details2";
  }

  @GetMapping("/details3")
  public String details3(Model model) {

    User user = new User("José Manuel", "Muñoz Manzano");
    user.setEmail("jmunoz@correo.es");

    model.addAttribute("title", "Hola Mundo Spring Boot");
    model.addAttribute("user", user);

    return "details3";
  }

  @GetMapping("/list")
  public String list(ModelMap model) {    

    model.addAttribute("title", "Listado de usuarios");
    //model.addAttribute("users", users);
    return "list";
  }

  @ModelAttribute("users")
  public List<User> usersModel() {
    return Arrays.asList(new User("Pepa", "González"),
                         new User("Lalo", "Pérez", "lalo@correo.com"),
                         new User("Juanita", "Roy", "juana@correo.com"),
                         new User("José M.", "Doe"));
  }
}
