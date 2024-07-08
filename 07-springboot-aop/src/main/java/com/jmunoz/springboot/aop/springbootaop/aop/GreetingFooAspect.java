package com.jmunoz.springboot.aop.springbootaop.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// El order 1 es el primero en @Before y el último en @After
@Component
@Order(1)
@Aspect
public class GreetingFooAspect {

  private Logger logger = LoggerFactory.getLogger(getClass());

  // Hay que indicar el nombre de la clase y el método para usar el pointcut.
  // Si la clase con los pointcut estuviesen en otro package, también habría que indicarlo.
  @Before("GreetingServicePointcuts.greetingFooLoggerPointcut()")
  public void loggerBefore(JoinPoint joinPoint) {

    String method = joinPoint.getSignature().getName();
    String args = Arrays.toString(joinPoint.getArgs());
    logger.info("Antes: " + method + " invocado con los argumentos " + args);
  }

  @After("GreetingServicePointcuts.greetingFooLoggerPointcut()")
  public void loggerAfter(JoinPoint joinPoint) {

    String method = joinPoint.getSignature().getName();
    String args = Arrays.toString(joinPoint.getArgs());
    logger.info("Después: " + method + " invocado con los argumentos " + args);
  }
}
