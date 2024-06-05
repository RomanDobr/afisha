package org.javaacademy.afisha;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.javaacademy.afisha.dto.PlaceDto;
import org.javaacademy.afisha.service.PlaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureJdbc
@AutoConfigureMockMvc
class PlaceAfishaApplicationTests {
  @Autowired
  private PlaceService placeService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  @SneakyThrows
  void createPlaceSuccess() {
    clearDbAfisha();
    String jsonPlace = objectMapper.writeValueAsString(new PlaceDto(
			"Фильм",
			"ул. 1Мая",
			"Ижевск"));
	RestAssured
			.given()
			.body(jsonPlace)
			.contentType(ContentType.JSON)
			.post("/api/v1/place/new")
			.then()
			.statusCode(201);
	boolean isTrue = placeService.findAllPlace()
			.stream()
			.anyMatch(place -> place.getName().equals("Фильм"));
	Assertions.assertTrue(isTrue);
  }

  @Test
  @SneakyThrows
  void findAllPlaceSuccess() {
    createPlaceSuccess();
	TypeRef<List<PlaceDto>> refDtos = new TypeRef<>() {
	};
	List<PlaceDto> placeDtos = RestAssured
			.given()
			.get("/api/v1/place")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(refDtos);
	Assertions.assertTrue(placeDtos.size() > 0);
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
