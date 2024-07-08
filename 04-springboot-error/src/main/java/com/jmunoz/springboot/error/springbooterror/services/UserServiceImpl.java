package com.jmunoz.springboot.error.springbooterror.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmunoz.springboot.error.springbooterror.models.domain.User;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private List<User> users;

  @Override
  public List<User> findAll() {
    return users;
  }

  @Override
  public User findById(Long id) {
    User user = null;

    for (var u : users) {
      if (u.getId().equals(id)) {
        user = u;
        break;
      }
    }
    
    return user;
  }

  @Override
  public Optional<User> findByIdOptional(Long id) {
    User user = null;
    for (User u : users) {
      if (u.getId().equals(id)) {
        user = u;
        break;
      }
    }

    // Devuelve un Optional.empty() si user es nulo o Optional.of(user) si user no es nulo
    return Optional.ofNullable(user);
  }

  @Override
  public Optional<User> findByIdFunctional(Long id) {
    // Si no lo encuentra, tendremos un empty()
    return users.stream().filter(u -> u.getId().equals(id)).findFirst();
  }
}
