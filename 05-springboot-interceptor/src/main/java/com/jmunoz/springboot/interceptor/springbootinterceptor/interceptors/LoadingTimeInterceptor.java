package com.jmunoz.springboot.interceptor.springbootinterceptor.interceptors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("timeInterceptor")
public class LoadingTimeInterceptor implements HandlerInterceptor {

  // Hay que indicar el mismo nombre que la clase interceptora
  private static final Logger logger = LoggerFactory.getLogger(LoadingTimeInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    HandlerMethod controller = ((HandlerMethod)handler);
    logger.info("LoadingTimeInterceptor: preHandle() entrando ...." + controller.getMethod().getName());

    long start = System.currentTimeMillis();
    request.setAttribute("start", start);

    // Simulamos un delay para la tardanza de la ejecución
    Random random = new Random();
    int delay = random.nextInt(500);
    Thread.sleep(delay);

    // Ejemplo devolución de false en interceptor. Enviar json personalizado.
    if (delay > 400) {
      Map<String, String> json = new HashMap<>();
      json.put("error", "No tiene acceso a esta página!");
      json.put("date", new Date().toString());

      // Para transformar manualmente a un texto con estructura JSON y pasarlo a la response de la petición.
      ObjectMapper mapper = new ObjectMapper();
      String jsonString = mapper.writeValueAsString(json);
      response.setContentType("application/json");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(jsonString);

      return false;
    }

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      @Nullable ModelAndView modelAndView) throws Exception {

    long end = System.currentTimeMillis();
    long start = (long) request.getAttribute("start");
    long result = end - start;

    logger.info("Tiempo transcurrido: " + result + " milisegundos");

    // Para saber el método que está interceptando, obtenemos el controller y luego su método
    logger.info("LoadingTimeInterceptor: postHandle() saliendo ...." + ((HandlerMethod)handler).getMethod().getName());
  }
}
