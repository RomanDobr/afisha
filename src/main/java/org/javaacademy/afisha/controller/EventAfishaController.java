package org.javaacademy.afisha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.dto.EventDto;
import org.javaacademy.afisha.entity.Event;
import org.javaacademy.afisha.service.EventService;
import org.javaacademy.afisha.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@Tag(name = "Сервис по работе с планируемыми мероприятиями в городе",
     description = "Методы по работе с событиями и создание билетов")
public class EventAfishaController {
  private final EventService eventService;
  private final PlaceService placeService;

  @GetMapping()
  @Operation(summary = "Поиск всех мероприятий")
  @ApiResponse(responseCode = "200",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = EventDto.class)))
  public ResponseEntity<List<EventDto>> findAllEvent() {
    return ResponseEntity.status(200).body(getEventsDtoRs(eventService.findAllEvents()));
  }

  @PostMapping("/new")
  @Operation(summary = "Создание мероприятия")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<String> createEvent(@RequestBody EventDto eventDto) {
    String response = eventService.createEvent(
                eventDto.getNameEvent(),
                eventDto.getPlaceName(),
                eventDto.getPrice(),
                LocalDateTime.now(),
                eventDto.getQty());
    return ResponseEntity.status(201).body(response);
  }

  private EventDto eventToDtoConvert(Event event) {
    return new EventDto(
            event.getName(),
            placeService.getPlaceById(event.getPlaceId()).getName(),
            event.getEventDate(),
            event.getPlaceId());
  }

  private List<EventDto> getEventsDtoRs(List<Event> events) {
    return events.stream().map(this::eventToDtoConvert).toList();
  }
}
