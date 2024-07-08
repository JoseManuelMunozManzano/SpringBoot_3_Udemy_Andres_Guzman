package com.jmunoz.curso.springboot.webapp.springbootweb.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmunoz.curso.springboot.webapp.springbootweb.models.dto.ParamDto;
import com.jmunoz.curso.springboot.webapp.springbootweb.models.dto.ParamMixDto;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/params")
public class RequestParamsController {
  
  @GetMapping("/foo")
  public ParamDto foo(@RequestParam(required = false, defaultValue = "Hola!!", name = "mensaje") String message) {
    
    ParamDto param = new ParamDto();
    param.setMessage(message);

    return param;
  }

  @GetMapping("/bar")
  public ParamMixDto bar(@RequestParam String text, @RequestParam Integer code) {

    ParamMixDto params = new ParamMixDto();
    params.setMessage(text);
    params.setCode(code);

    return params;
  }

  @GetMapping("/request")
  public ParamMixDto request(HttpServletRequest request) {

    Integer code = 0; 
    
    try {
      code = Integer.parseInt(request.getParameter("code"));
    } catch (NumberFormatException e) {
      // Por defecto code vale 0
    }

    ParamMixDto params = new ParamMixDto();
    params.setCode(code);
    params.setMessage(request.getParameter("message"));

    return params;
  }
}
