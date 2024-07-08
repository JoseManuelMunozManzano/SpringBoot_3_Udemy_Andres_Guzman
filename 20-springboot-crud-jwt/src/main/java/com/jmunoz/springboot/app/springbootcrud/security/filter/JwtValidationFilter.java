package com.jmunoz.springboot.app.springbootcrud.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.springboot.app.springbootcrud.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.jmunoz.springboot.app.springbootcrud.security.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

  // Basic Authentication Filter tiene un constructor que recibe el AuthenticationManager
  public JwtValidationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    // Obtener el token de la cabecera (que es donde se envía)
    String header = request.getHeader(HEADER_AUTHORIZATION);

    // Para las página públicas no nos envian token.
    // También puede que me lo envíen sin el texto "Bearer ".
    // Omitimos el filtro, continuando con los filtros que sigan y nos salimos.
    if (header == null || !header.startsWith(PREFIX_TOKEN)) {
      chain.doFilter(request, response);
      return;
    }

    // Eliminamos el texto "Bearer " para quedarme solo con el token.
    String token = header.replace(PREFIX_TOKEN, "");

    // Validar el token.
    // Obtenemos el payload para autenticarnos para ver si tenemos autorización a los recursos protegidos.
    try {
      Claims claims = Jwts.parser().verifyWith((SecretKey) SECRET_KEY).build().parseSignedClaims(token).getPayload();

      // Obtenemos el username.
      // En JwtAuthenticationFilter.java, en Jwst.builder() añadimos el username. Lo obtenemos de ahí.
      String username = claims.getSubject();
      // Esta otra forma de obtener el username está disponible porque añadimos a los claims (payload) el username
      // en JwtAuthenticationFilter.java
      // String username = (String) claims.get("username");

      // Obtenemos los roles. Aquí obtenemos un objeto String con formato JSON que tenemos que convertir
      // a un objeto de tipo GrantedAuthority.
      Object authoritiesClaims = claims.get("authorities");

      // Indicar que, tal como está, esto falla porque SimpleGrantedAuthority busca en el JSON un atributo de tipo "role" 
      // y nosotros mandamos en el JSON el texto "authority".
      // Esto significa que tenemos que modificar el constructor usando lo que se conoce como Mixin. La idea es personalizar el
      // constructor de SimpleGrantedAuthorities para que espere el atributo "authority" y no "role"
      // Ver SimpleGrantedAuthorityJsonCreator.java
      // Y con addMixIn configuramos la clase Mixin.
      //    Primero indicamos la clase original y luego la nuestra.
      Collection<? extends GrantedAuthority> authorities = Arrays.asList(
            new ObjectMapper()
              .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
              .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

      // Validamos el token para iniciar sesión.
      // El segundo parámetro es el password, que aquí no aplica (solo se valida cuando creamos el token) y por eso indicamos null.
      // El tercer parámetro son los roles, pero del tipo Collection<GrantedAuthority>
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

      // Nos autenticamos.
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

      // Continuamos con los demás filtros que haya.
      chain.doFilter(request, response);

    } catch (JwtException e) {
      // Si no valida el token enviamos este error.
      Map<String, String> body = new HashMap<>();
      body.put("error", e.getMessage());
      body.put("message", "El token JWT es inválido");

      response.getWriter().write(new ObjectMapper().writeValueAsString(body));
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(CONTENT_TYPE);
    }
  }
}
