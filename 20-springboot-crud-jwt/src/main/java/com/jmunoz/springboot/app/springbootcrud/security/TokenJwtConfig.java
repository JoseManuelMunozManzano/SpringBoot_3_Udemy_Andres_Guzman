package com.jmunoz.springboot.app.springbootcrud.security;

import java.security.Key;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// Clase de configuración
public class TokenJwtConfig {

  // El valor está cogido de https://github.com/jwtk/jjwt
  // Así se genera dinámicamente, pero cambia cada vez que se reinicia la aplicación.
  //
  // private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

  // Para que la key sea fija.
  public static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("algunaLaveSecretamuYLargaljdsfljsdfhfja34ro23jjajdf"));

  public static final String PREFIX_TOKEN = "Bearer ";
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String CONTENT_TYPE = "application/json";
}
