package org.javaacademy.afisha.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaacademy.afisha.entity.Ticket;
import org.javaacademy.afisha.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
  private final TicketRepository ticketRepository;

  public int salesByTicket(String email, String nameEvent, int qty, BigDecimal price) {
    int sales = ticketRepository.salesByTicket(email, nameEvent, qty, price);
    if (sales != 0) {
      log.info("Продано!");
    }
    return sales;
  }

  public List<Ticket> findAllTicket() {
    return ticketRepository.fundAllTicket();
  }
}
