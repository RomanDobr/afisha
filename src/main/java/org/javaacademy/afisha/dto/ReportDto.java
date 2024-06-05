package org.javaacademy.afisha.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ReportDto {
  private final String nameEvent;
  private final Integer eventTypeId;
  private final Integer qty;
  private final BigDecimal sumSelledTickets;
}
