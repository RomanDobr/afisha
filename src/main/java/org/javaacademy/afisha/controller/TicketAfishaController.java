package org.javaacademy.afisha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.dto.TicketDto;
import org.javaacademy.afisha.entity.Ticket;
import org.javaacademy.afisha.service.EventService;
import org.javaacademy.afisha.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
@Tag(name = "Сервис по продаже билетов на мероприятия в городе",
        description = "Методы по продажи билетов")
public class TicketAfishaController {
  private final TicketService ticketService;
  private final EventService eventService;

  @GetMapping()
  @Operation(summary = "Поиск всех проданных билетов")
  @ApiResponse(responseCode = "200",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = TicketDto.class)))
  public ResponseEntity<List<TicketDto>> findAllTicket() {
    return ResponseEntity.status(200).body(getTicketDtoRs(ticketService.findAllTicket()));
  }

  @PostMapping("/new")
  @Operation(summary = "Продажа билета")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<String> salesByTicket(@RequestBody TicketDto ticketDto) {
    int result = ticketService.salesByTicket(ticketDto.getClientEmail(),
             ticketDto.getEventName(),
             ticketDto.getQty(),
             ticketDto.getPrice());
    if (result > 0) {
      return ResponseEntity.status(201).body("Продано");
    }
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Билетов нет");
  }

  private TicketDto ticketToDtoConvert(Ticket ticket) {
    return new TicketDto(eventService.getEventById(ticket.getEventId()).getName(),
             ticket.getClientEmail(),
             1);
  }

  private List<TicketDto> getTicketDtoRs(List<Ticket> tickets) {
    return tickets.stream().map(this::ticketToDtoConvert).toList();
  }
}
