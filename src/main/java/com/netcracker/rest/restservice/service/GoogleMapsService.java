package com.netcracker.rest.restservice.service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class GoogleMapsService {
//    private String apiKey;
//
//    public GoogleMapsService(@Value("${googleMaps.apiKey}") String apiKey) {
//        this.apiKey = apiKey;
//    }

    private final GeoApiContext context;

    @Autowired
    public GoogleMapsService(GeoApiContext context) {
        this.context = context;
    }

    public PlacesSearchResponse findPlaces(String lat, String lng, int radius) {
        try {
            LatLng currentPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            return PlacesApi.nearbySearchQuery(context, currentPosition).radius(radius).await();
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            return new PlacesSearchResponse();
        }
    }

    public PlacesSearchResponse findPlacesNextPage(String nextPageToken) {
        try {
            return PlacesApi.nearbySearchNextPage(context, nextPageToken).await();
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            return new PlacesSearchResponse();
        }
    }
}
