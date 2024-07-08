package com.jmunoz.curso.springboot.webapp.springbootweb.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmunoz.curso.springboot.webapp.springbootweb.models.User;
import com.jmunoz.curso.springboot.webapp.springbootweb.models.dto.ParamDto;

@RestController
@RequestMapping("/api/var")
public class PathVariableController {

  @Value("${config.username}")
  private String username;

  // @Value("${config.message}")
  // private String message;

  @Value("${config.listOfValues}")
  // private String[] listOfValues;
  private List<String> listOfValues;

  @Value("${config.code}")
  private Integer code;

  // Para tratar como un String y que no lo convierta en array anidamos entre comillas simples. Entonces podemos trabajar con los métodos propios de un String.
  @Value("#{'${config.listOfValues}'.split(',')}")
  private List<String> valueList;

  @Value("#{'${config.listOfValues}'.toUpperCase()}")
  private String valueString;

  @Value("#{${config.valuesMap}}")
  private Map<String, Object> valuesMap;

  @Value("#{${config.valuesMap}.product}")
  private String product;

  @Value("#{${config.valuesMap}.price}")
  private Long price;

  @Autowired
  private Environment environment;
  
  @GetMapping({"baz", "/baz/{message}"})
  public ParamDto baz(@PathVariable(required = false) String message) {

    ParamDto param = new ParamDto();
    param.setMessage(message);

    return param;
  }

  @GetMapping("/mix/{product}/{id}")
  public Map<String, Object> mixPathVariable(@PathVariable String product, @PathVariable Long id) {

    Map<String, Object> json = new HashMap<>();
    json.put("product", product);
    json.put("id", id);

    return json;
  }

  @PostMapping("/create")
  public User create(@RequestBody User user) {

    // Hacer algo con el usuario, como un save en la BBDD
    user.setName(user.getName().toUpperCase());

    return user;
  }

  @GetMapping("/values")
  public Map<String, Object> values(@Value("${config.message}") String message) {

    Long code2 = environment.getProperty("config.code", Long.class);

    Map<String, Object> json = new HashMap<>();
    json.put("username", username);
    json.put("code", code);
    json.put("message", message);
    json.put("message2", environment.getProperty("config.message"));
    // json.put("code2", Integer.valueOf(environment.getProperty("config.code", "0")));
    json.put("code2", code2);
    json.put("listOfValues", listOfValues);
    json.put("valueList", valueList);
    json.put("valueString", valueString);
    json.put("valuesMap", valuesMap);
    json.put("product", product);
    json.put("price", price);

    return json;
  }
}
