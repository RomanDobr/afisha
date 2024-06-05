package org.javaacademy.afisha;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.javaacademy.afisha.dto.EventDto;
import org.javaacademy.afisha.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureJdbc
@AutoConfigureMockMvc
public class EventAfishaApplicationTests {
  @Autowired
  private EventService eventService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  @SneakyThrows
  void createEventSuccess() {
    clearDbAfisha();
    jdbcTemplate.update("""
                INSERT INTO application.place (name, address, city) values (
                'Фильм',
                'ул. 1Мая',
                'Ижевск');
                """);
    EventDto eventDto = new EventDto("cinema", "Фильм", LocalDateTime.now(), 1);
    eventDto.setPrice(BigDecimal.TEN);
    eventDto.setQty(100);
    String jsonEvent = objectMapper.writeValueAsString(eventDto);
    RestAssured
                .given()
                .body(jsonEvent)
                .contentType(ContentType.JSON)
                .post("/api/v1/event/new")
                .then()
                .statusCode(201);

    boolean isTrue = eventService.findAllEvents()
                .stream()
                .anyMatch(event -> event.getName().equals("cinema"));
    Assertions.assertTrue(isTrue);
  }

  @Test
  @SneakyThrows
  void findAllEventSuccess() {
    createEventSuccess();
    TypeRef<List<EventDto>> refDtos = new TypeRef<>() {
    };
    List<EventDto> eventDtos = RestAssured
                .given()
                .get("/api/v1/event")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(refDtos);
    Assertions.assertTrue(eventDtos.size() > 0);
  }

  private void clearDbAfisha() {
    jdbcTemplate.execute("""
                TRUNCATE 
                application.event,
                application.ticket,
                application.place
                RESTART IDENTITY;
                """);
  }
}
