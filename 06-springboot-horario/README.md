# INTERCEPTORES HTTP - HORARIO DE ACCESO A CLIENTES

Ejemplo de uso de interceptores.

Dejamos pasar al usuario si accede a la app entre las 14:00 y las 19:00

## Qué temas se tratan

- Uso de controlador anotado con @RestController
- @GetMapping()
- ResponseEntity<?>
- HttpServletRequest
- Map
- application.properties
- Uso de interceptor anotado con @Component y que implementa HandlerInterceptor
- Calendar
- Inyección de properties con la anotación @Value
- StringBuilder
- request.setAttribute()
- request.getAttribute()
- ObjectMapper
- Convertir respuesta a JSON
  - response.setContentType("application/json");
  - response.setStatus(401);
  - response.getWriter().write(mapper.writeValueAsString(data));
- Registrar interceptor usando clase de configuración de Spring, anotado con @Configuration, e implementamos WebMvcConfigure
- Hacer override del método addInterceptors
- registry.addInterceptor(calendar).addPathPatterns("/foo");
- @Autowired
- HandlerInterceptor
- @Qualifier

## Testing

Acceder con Postman, con GET a la ruta:

- `http://localhost:8080/foo`

Cambiar, en application.properties, las horas open y close para obtener cada uno de los dos resultados.
