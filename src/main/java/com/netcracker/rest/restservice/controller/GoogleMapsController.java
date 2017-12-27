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
import java.util.Collections;
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
                                  @RequestParam(name = "radius", required = false) Integer radius,
                                  @RequestParam(name = "duration", required = false) Integer hours,
                                  @RequestParam(name = "type") String type,
                                  @RequestParam(name = "carGasolinePrice") Double carGasolinePrice,
                                  @RequestParam(name = "carGasolineConsumption") Double carGasolineConsumption
    ) throws InterruptedException, ApiException, IOException {
        if(radius == null) {
            radius = 5000;
        }
        if(hours == null) {
            hours = 600;
        }
        if(carGasolinePrice == null) {
            carGasolinePrice = 35d;
        }
        if(carGasolineConsumption == null) {
            carGasolineConsumption = 0.1;
        }
        return googleMapsService.findPlaces(lat, lng, radius, type, hours, carGasolinePrice, carGasolineConsumption);
    }
    @RequestMapping(value = "findLucky", method = RequestMethod.GET)
    public List<Place> findLuckyPlace(@RequestParam(name = "lat") String lat,
                                @RequestParam(name = "lng") String lng) throws InterruptedException, ApiException, IOException {

        return Collections.singletonList(googleMapsService.findLuckyPlace(lat, lng));
    }
}
