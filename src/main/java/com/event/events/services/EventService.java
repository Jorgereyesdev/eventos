package com.event.events.services;

import com.event.events.entities.Event;
import com.event.events.repositories.IEventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private IEventRepository eventRepository;

    public Event crearEvento(Event event) {
        if (event.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del evento no puede estar en el pasado.");
        }

        if (event.getCapacidad() < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa.");
        }
        return eventRepository.save(event);
    }

    public List<Event> obtenerTodosLosEventos() {
        return eventRepository.findAll();
    }

    public Optional<Event> obtenerEventoPorId(Integer id) {
        return eventRepository.findById(id);
    }

    @Transactional
    public Event modificarEvento(Integer id, Event event) {
        return eventRepository
                .findById(id)
                .map(eventoExistente -> {
                    eventoExistente.setNombre(event.getNombre());
                    eventoExistente.setFecha(event.getFecha());
                    eventoExistente.setUbicacion(event.getUbicacion());
                    eventoExistente.setCapacidad(event.getCapacidad());

                    if (eventoExistente.getFecha().isBefore(LocalDate.now())) {
                        throw new IllegalArgumentException("La fecha del evento no puede estar en el pasado.");
                    }

                    if (eventoExistente.getCapacidad() < 0) {
                        throw new IllegalArgumentException("La capacidad no puede ser negativa.");
                    }
                    return eventRepository.save(eventoExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + id));
    }

    public void eliminarPorId(Integer id) {
        eventRepository.deleteById(id);
    }
}
