package org.javaacademy.afisha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.dto.PlaceDto;
import org.javaacademy.afisha.entity.Place;
import org.javaacademy.afisha.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
@Tag(name = "Сервис по работе с планируемыми местами где будут проходить мероприятия в городе",
        description = "Методы по работе с местом мероприятия")
public class PlaceAfishaController {
  private final PlaceService placeService;

  @GetMapping()
  @Operation(summary = "Поиск всех мест мероприятий")
  @ApiResponse(responseCode = "200",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PlaceDto.class)))
  public ResponseEntity<List<PlaceDto>> findAllPlace() {
    return ResponseEntity.status(200).body(getPlaceDtoRs(placeService.findAllPlace()));
  }

  @PostMapping("/new")
  @Operation(summary = "Создание места мероприятия")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<String> createPlace(@RequestBody PlaceDto placeDto) {
    String response = placeService.createPlace(
            placeDto.getName(),
            placeDto.getAddress(),
            placeDto.getCity());
    return ResponseEntity.status(201).body(response);
  }

  private PlaceDto placeToDtoConvert(Place place) {
    return new PlaceDto(place.getName(), place.getAddress(), place.getCity());
  }

  private List<PlaceDto> getPlaceDtoRs(List<Place> places) {
    return places.stream().map(this::placeToDtoConvert).toList();
  }
}
