package com.jmunoz.springboot.error.springbooterror.services;

import java.util.List;
import java.util.Optional;

import com.jmunoz.springboot.error.springbooterror.models.domain.User;

public interface UserService {

  List<User> findAll();
  User findById(Long id);

  Optional<User> findByIdOptional(Long id);

  Optional<User> findByIdFunctional(Long id);
}
