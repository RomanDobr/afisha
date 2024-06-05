package org.javaacademy.afisha.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventDto {
  private final String nameEvent;
  private final String placeName;
  private BigDecimal price;
  private final LocalDateTime eventDate;
  private final Integer placeId;
  private Integer qty;
}
