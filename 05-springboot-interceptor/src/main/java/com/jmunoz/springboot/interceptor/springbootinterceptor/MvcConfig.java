package com.jmunoz.springboot.interceptor.springbootinterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  // Como el interceptor es un componente de Spring se puede inyectar aquí.
  // Como de forma interna Spring usa muchos interceptores, tenemos que indicar cual se va a inyectar.
  @Autowired
  @Qualifier("timeInterceptor")
  private HandlerInterceptor timeInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(timeInterceptor).addPathPatterns("/app/bar", "/app/foo");
    // registry.addInterceptor(timeInterceptor).excludePathPatterns("/app/baz");
  }

}
