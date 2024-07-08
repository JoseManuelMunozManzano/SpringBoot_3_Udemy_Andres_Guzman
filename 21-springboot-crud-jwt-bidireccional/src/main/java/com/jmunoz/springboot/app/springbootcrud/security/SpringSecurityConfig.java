package com.jmunoz.springboot.app.springbootcrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Vamos a validar los requests, autorizar y denegar permisos.
  // Se inyecta de forma automática un objeto de Spring de tipo HttpSecurity.
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // Se le pasa un callback con las rutas a las que queremos dar seguridad.
    // Estamos diciendo que damos permiso (es público) a todo el mundo a un GET hacia /api/users, 
    // y un POST hacia /api/users/register,
    // y que cualquier otra petición necesita autenticación.
    //
    // Indicamos el token cross site request forgery (csrf) como una seguridad extra, sobre todo en los formularios.
    // Se deshabilita porque estamos trabajando con API Rest, no con vistas Thymeleaf ni JSP...
    //
    // SessionCreationPolicy: La sesión http donde se guarda la sesión de usuario, es por defecto con estado.
    // Aquí no porque la sesión se va a enviar con el token. Con cada request enviamos el token con algunos
    // datos de los usuarios para que se pueda autenticar. Por eso se indica STATELESS
    return http.authorizeHttpRequests((auth) -> auth
      .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
      .anyRequest().authenticated())
      .csrf(config -> config.disable())
      .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .build();
  }
}
