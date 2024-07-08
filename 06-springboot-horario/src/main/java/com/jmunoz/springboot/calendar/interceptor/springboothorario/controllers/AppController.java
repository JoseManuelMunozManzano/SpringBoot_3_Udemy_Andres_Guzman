package com.jmunoz.springboot.calendar.interceptor.springboothorario.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AppController {

  @GetMapping("/foo")
  public ResponseEntity<?> foo(HttpServletRequest request) {

    Map<String, Object> data = new HashMap<>();
    data.put("title", "Bienvenidos al sistema de atención!");
    data.put("time", new Date().toString());

    // Data que proviene del interceptor
    data.put("message", request.getAttribute("message"));

    return ResponseEntity.ok(data);
  }
}
