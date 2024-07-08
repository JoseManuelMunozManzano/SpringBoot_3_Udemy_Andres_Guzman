package com.jmunoz.springboot.app.springbootcrud.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.jmunoz.springboot.app.springbootcrud.security.filter.JwtAuthenticationFilter;
import com.jmunoz.springboot.app.springbootcrud.security.filter.JwtValidationFilter;

// Se indica @EnableMethodSecurity para que funcione la autorización por anotación en el controlador.
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

  // Inyectamos el mismo componente que se uso en JwtAuthenticationFilter.
  // Nos permite generar y obtener el authenticationManager de nuestra aplicación de Spring Security.
  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

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
    // datos de los usuarios para que se pueda autenticar. Por eso se indica STATELESS.
    return http.authorizeHttpRequests((auth) -> auth
      .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
      // Solo los usuarios con role ROLE_ADMIN pueden acceder a estos endpoints.
      // Como puede verse, no se indica la palabra ROLE_, solo ADMIN, USER...
      // Por debajo, el hasRole agrega este prefijo ROLE_
      //
      // Se comenta porque se ha hecho con anotaciones en las clases controller
      //
      // .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
      // .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
      // .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/{id}").hasAnyRole("ADMIN", "USER")
      // .requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole("ADMIN")
      // .requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole("ADMIN")
      .anyRequest().authenticated())
      .addFilter(new JwtAuthenticationFilter(authenticationManager()))  // Añadimos filtro
      .addFilter(new JwtValidationFilter(authenticationManager()))  // Añadimos filtro
      .csrf(config -> config.disable())
      // Parte de configuración de cors de SpringSecurity
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .build();
  }

  // Configurando de forma programática los beans para evitar el error de CORS.
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
    // Es importante también configurar las cabeceras
    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    config.setAllowCredentials(true);

    // Creamos una instancia de una configuración concreta de la interface CorsConfigurationSource
    // Se indica donde se va a aplicar esta configuración. En este ejemplo, en la raiz "/**"
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }

  // Configuración de un filtro de Spring que queremos que se ejecute siempre en todas las rutas.
  // Lo unimos a la configuración anterior de cors.
  @Bean
  FilterRegistrationBean<CorsFilter> corsFilter() {
    FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));

    // Le metemos una muy alta prioridad en la generación de este bean.
    corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return corsBean;
  }

}
