package com.jmunoz.springboot.aop.springbootaop.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class GreetingServicePointcuts {

  // Deben ser métodos públicos
  @Pointcut("execution(* com.jmunoz.springboot.aop.springbootaop..*.*(..))")
  public void greetingLoggerPointcut() {}

  @Pointcut("execution(* com.jmunoz.springboot.aop.springbootaop.services.GreetingService.*(..)) ")
  public void greetingFooLoggerPointcut() {}

}
