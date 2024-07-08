package com.jmunoz.springboot.app.springbootcrud.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.app.springbootcrud.entities.User;
import com.jmunoz.springboot.app.springbootcrud.repositories.UserRepository;

// Le hemos puesto este nombre, comenzando con JPA, porque vamos a implementar UserDetailsService con JPA.
@Service
public class JpaUserDetailsService implements UserDetailsService {

  // Inyectamos el componente (en el constructor) que nos permite obtener el username.
  // Usaremos UserRepository para buscar el username y lo convertiremos a UserDetails.
  private UserRepository repository;

  public JpaUserDetailsService(UserRepository repository) {
    this.repository = repository;
  }

  // UserDetails es una clase User, pero de Spring Security.
  // El parámetro username va a venir de nuestro formulario Login del frontend (o Postman)
  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<User> userOptional = repository.findByUsername(username);

    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema!", username));
    }

    User user = userOptional.orElseThrow();

    // Tenemos que convertir la lista de tipo Role a una lista de tipo GrantedAuthority.
    // GrantedAuthority es una interface y SimpleGrantedAuthority es una implementación.
    List<GrantedAuthority> authorities = user.getRoles().stream()
      .map(role -> new SimpleGrantedAuthority(role.getName()))
      .collect(Collectors.toList());

    // Al devolver la instancia, tenemos que indicar varios parámetros:
    //  - username
    //  - password
    //  - isEnabled
    //  - tres trues seguidos
    //       - accountNonExpired
		//       - credentialsNonExpired
		//       - accountNonLocked
    //  - authorities (los roles)
    return new org.springframework.security.core.userdetails.User(
                      user.getUsername(),
                      user.getPassword(),
                      user.isEnabled(),
                      true,
                      true,
                      true,
                      authorities
                      );
  }
  
}
