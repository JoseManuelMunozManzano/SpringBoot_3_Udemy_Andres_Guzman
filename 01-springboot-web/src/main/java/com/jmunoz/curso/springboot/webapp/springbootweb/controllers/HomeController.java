package com.jmunoz.curso.springboot.webapp.springbootweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  
  @GetMapping({"", "/", "/home"})
  public String home() {
    return "redirect:/list";
  }

  @GetMapping("/forward")
  public String home_forward() {
    return "forward:/det";
  }
}
