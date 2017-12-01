package com.netcracker.rest.restservice.controller;

import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.netcracker.rest.restservice.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "api/places")
public class GoogleMapsController {

    private final GoogleMapsService googleMapsService;

    @Autowired
    public GoogleMapsController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public List<PlacesSearchResult> findPlaces(@RequestParam(name = "lat") String lat,
                                               @RequestParam(name = "lng") String lng,
                                               @RequestParam(name = "radius") int radius) {
        PlacesSearchResponse places = googleMapsService.findPlaces(lat, lng, radius);
        return new ArrayList<>(Arrays.asList(places.results));
    }
}
