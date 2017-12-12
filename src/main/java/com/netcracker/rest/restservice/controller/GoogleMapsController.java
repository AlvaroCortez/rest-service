package com.netcracker.rest.restservice.controller;

import com.google.maps.model.PlacesSearchResponse;
import com.netcracker.rest.restservice.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/places")
public class GoogleMapsController {

    private final GoogleMapsService googleMapsService;

    @Autowired
    public GoogleMapsController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public PlacesSearchResponse findPlaces(@RequestParam(name = "lat") String lat,
                                           @RequestParam(name = "lng") String lng,
                                           @RequestParam(name = "radius") int radius) {
        PlacesSearchResponse places = googleMapsService.findPlaces(lat, lng, radius);
        return places;
    }

    @RequestMapping(value = "findNext", method = RequestMethod.GET)
    public PlacesSearchResponse findPlacesNextPage(@RequestParam(name = "nextPageToken") String nextPageToken) {
        PlacesSearchResponse places = googleMapsService.findPlacesNextPage(nextPageToken);
        return places;
    }
}
