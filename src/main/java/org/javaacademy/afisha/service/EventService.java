package org.javaacademy.afisha.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Event;
import org.javaacademy.afisha.repository.EventRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;

  public String createEvent(String nameEvent,
                            String namePlace,
                            BigDecimal price,
                            LocalDateTime time,
                            int qty) {
    eventRepository.createEvent(nameEvent, namePlace, price, qty);
    return "Событие создано";
  }

  public Event getEventById(Integer id) {
    return eventRepository.getEventById(id);
  }

  public List<Event> findAllEvents() {
    return eventRepository.findAllEvents();
  }
}
