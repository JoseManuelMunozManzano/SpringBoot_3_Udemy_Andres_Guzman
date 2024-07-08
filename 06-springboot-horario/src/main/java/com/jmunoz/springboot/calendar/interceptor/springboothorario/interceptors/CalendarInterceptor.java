package com.jmunoz.springboot.calendar.interceptor.springboothorario.interceptors;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CalendarInterceptor implements HandlerInterceptor {

  // Para poder inyectar los valores de properties, esta clase tiene que ser un componente.
  @Value("${config.calendar.open}")
  private Integer open;

  @Value("${config.calendar.close}")
  private Integer close;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    System.out.println(hour);

    if (hour >= open && hour < close) {
      StringBuilder message = new StringBuilder("Bienvenidos al horario de atención al cliente");
      message.append(". Atendemos desde las ");
      message.append(open);
      message.append("h. ");
      message.append("hasta las ");
      message.append(close);
      message.append("h. ");
      message.append("Gracias por su visita!");

      request.setAttribute("message", message.toString());

      return true;
    }

    StringBuilder message = new StringBuilder("Cerrado, fuera del horario de atención. ");
    message.append("Por favor, visítenos desde las ");
    message.append(open);
    message.append("h. hasta las ");
    message.append(close);
    message.append("h. Gracias!");

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> data = new HashMap<>();
    data.put("message", message.toString());
    data.put("date", new Date().toString());

    // Convertimos a JSON
    response.setContentType("application/json");
    response.setStatus(401);
    response.getWriter().write(mapper.writeValueAsString(data));

    return false;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
  }
}
