package com.jmunoz.springboot.aop.springbootaop.services;

public interface GreetingService {

  String sayHello(String person, String phrase);

  String sayHelloError(String person, String phrase);
}
