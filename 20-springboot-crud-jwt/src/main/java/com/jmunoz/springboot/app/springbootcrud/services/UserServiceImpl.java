package com.jmunoz.springboot.app.springbootcrud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.app.springbootcrud.entities.Role;
import com.jmunoz.springboot.app.springbootcrud.entities.User;
import com.jmunoz.springboot.app.springbootcrud.repositories.RoleRepository;
import com.jmunoz.springboot.app.springbootcrud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  private PasswordEncoder passwordEncoder;

  public UserServiceImpl(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return (List<User>) userRepository.findAll();
  }

  @Override
  @Transactional
  public User save(User user) {

    // Antes de persistir tenemos que agregar los roles al usuario.
    // Siempre va a tener el ROLE_USER, y en User viene una bandera que indica si además tiene el ROLE_ADMIN.
    // Los roles que se vayan a asignar al usuario tienen que existir.
    Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
    List<Role> roles = new ArrayList<>();

    optionalRoleUser.ifPresent(roles::add);

    if (user.isAdmin()) {
      Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
      optionalRoleAdmin.ifPresent(roles::add);
    }

    user.setRoles(roles);

    // También tenemos que encriptar la clave usando bcrypt.
    String passwordEncoded = passwordEncoder.encode(user.getPassword());
    user.setPassword(passwordEncoded);

    return userRepository.save(user);
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
}
