package com.jmunoz.springboot.app.springbootcrud.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.springboot.app.springbootcrud.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Para importar todas las constantes de TokenJwtConfig.java de forma estática.
import static com.jmunoz.springboot.app.springbootcrud.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  // Mediante el request POST obtenemos el username y el password, que vienen en un JSON.
  // Tenemos que capturar ese JSON y convertirlo a un objeto entity User (deserialización)
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    // Nuestro User de la clase entity.
    User user = null;
    String username = null;
    String password = null;

    // El user lo obtenemos utilizando una clase especial llamada ObjectMapper.
    // Indicamos como primer parámetro el JSON que viene en el request como un stream de entrada.
    // Indicamos como segundo parámetro el tipo de entrada al cual vamos a convertir.
    // Lo importante es que los datos del JSON tenga los mismos atributos que la clase User,
    // en este caso username y password.
    try {
      user = new ObjectMapper().readValue(request.getInputStream(), User.class);
      username = user.getUsername();
      password = user.getPassword();
    } catch (StreamReadException e) {
      e.printStackTrace();
    } catch (DatabindException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

    // Este authenticate() por debajo lo que hace es llamar a JPAUserDetailsService.java
    // para que nos autentiquemos usando la BD, pasando el username y el password para compararlos.
    return authenticationManager.authenticate(authenticationToken);
  }

  // Si ha autenticado correctamente.
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    // Hacemos un cast a User de Spring Security, no de nuestro entity.
    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
    String username = user.getUsername();
    Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

    // Los claim son datos, parte del payload.
    // No indicar nunca información sensible como password o cuentas bancarias...
    // Se añade el username, pero es redundante.
    // Los roles los tenemos que serializar como una estructura JSON
    Claims claims = Jwts.claims()
      .add("authorities", new ObjectMapper().writeValueAsString(roles))
      .add("username", username)
      .build();

    // Para generar el token.
    // Cogido de: https://github.com/jwtk/jjwt
    String token = Jwts.builder()
              .subject(username)
              .claims(claims)
              .expiration(new Date(System.currentTimeMillis() + 3_600_000)) // expira en una hora
              .issuedAt(new Date()) // fecha actual
              .signWith(SECRET_KEY)
              .compact();

    // Devolvemos token al cliente en el Header.
    // Notar que se indica "Bearer "
    response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

    // Y también lo devolvemos como una respuesta JSON en el body, ahí sin el "Bearer "
    Map<String, String> body = new HashMap<>();
    body.put("token", token);
    body.put("username", username);
    body.put("message", String.format("Hola %s has iniciado sesión con éxito", username));

    // Escribimos en la respuesta. Usamos ObjectMapper para transformar a JSON (serializar)
    // e indicamos que es un json (contentType) y en el status que todo ha ido bien.
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setContentType(CONTENT_TYPE);
    response.setStatus(200);
  }

  // Lo que hacemos cuando la autenticación ha fallado
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {

    Map<String, String> body = new HashMap<>();
    body.put("message", "Error en la autenticación. Username o Password incorrectos!");
    // La causa del problema.
    body.put("error", failed.getMessage());

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(401);    // No Authorizado
    response.setContentType(CONTENT_TYPE);

  }
}
