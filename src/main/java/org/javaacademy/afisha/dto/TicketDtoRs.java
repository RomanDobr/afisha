package org.javaacademy.afisha.dto;

import lombok.Data;
import java.util.List;

@Data
public class TicketDtoRs {
  private final List<TicketDto> tickets;
}
