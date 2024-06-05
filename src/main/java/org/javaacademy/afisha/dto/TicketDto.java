package org.javaacademy.afisha.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketDto {
  private final String eventName;
  private final String clientEmail;
  private final Integer qty;
  private BigDecimal price;
}
