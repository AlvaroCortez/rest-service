package com.netcracker.rest.restservice.controller;

import com.google.maps.errors.ApiException;
import com.netcracker.rest.restservice.model.Place;
import com.netcracker.rest.restservice.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    public List<Place> findPlaces(@RequestParam(name = "lat") String lat,
                                  @RequestParam(name = "lng") String lng,
                                  @RequestParam(name = "radius") int radius,
                                  @RequestParam(name = "duration") int hours,
                                  @RequestParam(name = "type") String type
    ) throws InterruptedException, ApiException, IOException {
        return googleMapsService.findPlaces(lat, lng, radius, type, hours);
    }
}
