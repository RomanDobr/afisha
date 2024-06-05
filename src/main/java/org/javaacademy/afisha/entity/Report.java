package org.javaacademy.afisha.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Report {
  private String nameEvent;
  private Integer eventTypeId;
  private Integer qty;
  private BigDecimal sumSelledTickets;
}
