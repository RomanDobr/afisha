package org.javaacademy.afisha.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Place;
import org.javaacademy.afisha.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
  private final PlaceRepository placeRepository;

  public String createPlace(String namePlace, String address, String city) {
    placeRepository.createPlace(namePlace, address, city);
    return "Место проведения события создано";
  }

  public Place getPlaceById(int id) {
    return placeRepository.getPlaceId(id);
  }

  public List<Place> findAllPlace() {
    return placeRepository.findAllPlace();
  }
}
