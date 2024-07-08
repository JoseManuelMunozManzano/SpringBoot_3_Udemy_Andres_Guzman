package com.neimerc.springboot.client.controllers;

import com.neimerc.springboot.client.models.Message;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class AppController {

    // Para read y write (sería USER y ADMIN)
    @GetMapping("/list")
    public List<Message> list() {
        return Collections.singletonList(new Message("Test list"));
    }

    // Solo para write
    @PostMapping("/create")
    public Message create(@RequestBody Message message) {
        System.out.println("Mensaje guardado: " + message);
        return message;
    }

    // Para intercambiar el authorization code por un token para poder acceder
    // a nuestras rutas
    // Esta ruta es permitida para todos.
    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam String code) {
        return Collections.singletonMap("code", code);
    }
}
