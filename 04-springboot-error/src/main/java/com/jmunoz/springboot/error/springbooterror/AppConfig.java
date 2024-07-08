package com.jmunoz.springboot.error.springbooterror;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jmunoz.springboot.error.springbooterror.models.domain.User;

@Configuration
public class AppConfig {

  @Bean
  List<User> bootstrapUsers() {
    List<User> users =  new ArrayList<>();
    users.add(new User(1L, "José Manuel", "Muñoz"));
    users.add(new User(2L, "Adriana", "Mena"));
    users.add(new User(3L, "Tania", "Gutierrez"));
    users.add(new User(4L, "Ferney", "Ramírez"));
    users.add(new User(5L, "Juan Carlos", "Martín"));

    return users;
  }
}
