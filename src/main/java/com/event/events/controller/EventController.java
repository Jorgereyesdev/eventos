package com.event.events.controller;

import com.event.events.entities.Event;
import com.event.events.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventoServicio;

    @Autowired
    public EventController(EventService eventoServicio) {
        this.eventoServicio = eventoServicio;
    }

    @PostMapping
    public ResponseEntity<Event> crearEvento(@RequestBody Event evento) {
        return new ResponseEntity<>(eventoServicio.crearEvento(evento), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Event>> obtenerTodosLosEventos() {
        return new ResponseEntity<>(eventoServicio.obtenerTodosLosEventos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> obtenerEventoPorId(@PathVariable Integer id) {
        return eventoServicio.obtenerEventoPorId(id)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> modificarEvento(@PathVariable Integer id, @RequestBody Event event) {
        try {
            Event eventoActualizado = eventoServicio.modificarEvento(id, event);
            return new ResponseEntity<>(eventoActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Integer id) {
        eventoServicio.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}